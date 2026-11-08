#!/usr/bin/env bash
# Build par impact (approche C) : modules Gradle impactés par les changements depuis une base git.
# Graphe : tâche `projectGraph` (build-logic/convention/.../ProjectGraph.kt), lu dans Gradle et non dans les fichiers.
#
# Usage : ./scripts/affected-modules.sh [--base <ref>] [--format list|tasks|github]
#   --base    référence git de comparaison (défaut : origin/main). Base vide ou inconnue → tout est impacté.
#   --format  list   : un module impacté par ligne (défaut)
#             tasks  : tâches de tests unitaires des modules impactés, sur une ligne (pour ./gradlew)
#             github : lignes clé=valeur pour $GITHUB_OUTPUT (modules, tasks, android, android_test_tasks, ios)
# PROJECT_GRAPH_FILE permet de fournir le graphe déjà calculé (tests du script, sans Gradle).
set -euo pipefail
cd "$(dirname "$0")/.."

BASE="origin/main"; FORMAT="list"
while [ $# -gt 0 ]; do
  case "$1" in
    --base) BASE="${2:-}"; shift 2 ;;
    --format) FORMAT="${2:-}"; shift 2 ;;
    *) echo "argument inconnu : $1" >&2; exit 2 ;;
  esac
done

# Fichiers changés : commits depuis la base + arbre de travail + fichiers non suivis.
# "*" signifie « tout est impacté » (base absente, ex. premier push d'une branche).
CHANGED="$(mktemp)"; GRAPH="$(mktemp)"; trap 'rm -f "$CHANGED" "$GRAPH"' EXIT
if [ -n "$BASE" ] && [ "$BASE" != "0000000000000000000000000000000000000000" ] \
  && MB=$(git merge-base "$BASE" HEAD 2>/dev/null); then
  { git diff --name-only "$MB"; git ls-files --others --exclude-standard; } | sort -u >"$CHANGED"
else
  echo "base « $BASE » introuvable : tous les modules sont impactés" >&2
  echo "*" >"$CHANGED"
fi

if [ -n "${PROJECT_GRAPH_FILE:-}" ]; then
  cp "$PROJECT_GRAPH_FILE" "$GRAPH"
else
  ./gradlew projectGraph -q --console=plain >"$GRAPH"
fi

python3 - "$CHANGED" "$GRAPH" "$FORMAT" <<'PY'
import sys
changed_file, graph_file, fmt = sys.argv[1:4]
changed = [l.strip() for l in open(changed_file) if l.strip()]

modules, deps = {}, {}
for line in open(graph_file):
    parts = line.split()
    if len(parts) == 4 and parts[0] == "module":
        _, path, folder, test = parts
        modules[path] = {"dir": folder.rstrip("/") + "/", "test": test}
    elif len(parts) == 3 and parts[0] == "dep":
        deps.setdefault(parts[2], set()).add(parts[1])  # dépendance → dépendants
if not modules:
    sys.exit("graphe de projets vide : la tâche projectGraph n'a rien produit")

# Fichiers qui touchent tous les modules (configuration du build, CI de ces jobs).
GLOBAL_PREFIXES = ("gradle/", "build-logic/", "config/", ".github/")
GLOBAL_FILES = {"settings.gradle.kts", "build.gradle.kts", "gradle.properties", "gradlew", "gradlew.bat",
                "scripts/affected-modules.sh"}
# Fichiers sans effet sur le build ni sur les tests.
IGNORED_PREFIXES = ("docs/", "bench/", ".claude/", "scripts/bench/")
IGNORED_SUFFIXES = (".md",)
# Konsist lit les sources de tout le dépôt (konsist-tests.gradle.kts) : impacté par tout fichier Kotlin.
KONSIST = ":shared:konsist"

affected, ios, android_script, everything = set(), False, False, False
by_depth = sorted(modules.items(), key=lambda kv: -len(kv[1]["dir"]))
for f in changed:
    if f == "*" or f in GLOBAL_FILES or f.startswith(GLOBAL_PREFIXES):
        everything = True; break
    if f.startswith(IGNORED_PREFIXES) or f.endswith(IGNORED_SUFFIXES):
        continue
    if f.startswith("ios/") or f in ("scripts/test-ios.sh", "scripts/build-ios.sh", "scripts/setup-ios-tools.sh"):
        ios = True; continue
    if f in ("scripts/test-android-instrumented.sh", "scripts/build-android.sh"):
        android_script = True; continue
    owner = next((p for p, m in by_depth if f.startswith(m["dir"])), None)
    if owner is None:
        everything = True; break  # fichier inconnu : prudence, tout est impacté
    affected.add(owner)
    if f.endswith((".kt", ".kts")) and KONSIST in modules:
        affected.add(KONSIST)

if everything:
    affected, ios, android_script = set(modules), True, True
else:
    todo = list(affected)
    while todo:
        for dependant in deps.get(todo.pop(), ()):
            if dependant not in affected:
                affected.add(dependant); todo.append(dependant)

ordered = sorted(affected)
tasks = [f"{p}:{modules[p]['test']}" for p in ordered if modules[p]["test"] != "-"]
android_mods = [p for p in ordered if p.startswith(":android:")]
android = android_script or bool(android_mods)
if android_script and not android_mods:
    android_mods = sorted(p for p in modules if p.startswith(":android:"))
android_tasks = [f"{p}:connectedDebugAndroidTest" for p in android_mods]
# Le framework iOS exporte les modules KMP via le parapluie :shared ; s'il est impacté, l'iOS aussi.
ios = ios or ":shared" in affected

if fmt == "list":
    print("\n".join(ordered))
elif fmt == "tasks":
    print(" ".join(tasks))
elif fmt == "github":
    print(f"modules={' '.join(ordered)}")
    print(f"tasks={' '.join(tasks)}")
    print(f"android={'true' if android else 'false'}")
    print(f"android_test_tasks={' '.join(android_tasks)}")
    print(f"ios={'true' if ios else 'false'}")
else:
    sys.exit(f"format inconnu : {fmt}")
PY
