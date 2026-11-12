#!/usr/bin/env bash
# Ratio de hits du cache de build (0-100) sur la même révision : un build de remplissage, purge des sorties
# et des caches locaux, puis un build mesuré. Seul le cache distant (Gradle ou Bazel) peut alors servir.
# Sans cache distant configuré, le résultat est 0 par construction (avertissement sur stderr).
# Imprime `null` si un build échoue ou si aucune ligne de synthèse n'est trouvée : un 0 n'est jamais un échec masqué.
#   BENCH_CACHE_PRIME=0 : pas de build de remplissage (cache distant déjà rempli par l'appelant).
#   BENCH_LOG_DIR       : dossier des logs conservés (défaut : $ROOT/bench/logs, ignoré par git).
source "$(dirname "$0")/_lib.sh"
cd "$ROOT"
LOG_DIR="${BENCH_LOG_DIR:-$ROOT/bench/logs}"; mkdir -p "$LOG_DIR"
PRIME_LOG="$LOG_DIR/cache-hit-prime.log"; LOG="$LOG_DIR/cache-hit.log"

build_all() {  # build_all <log> : builds Android puis iOS, logs complets dans <log>, retourne 1 au premier échec
  : >"$1"
  BENCH_CAPTURE_LOG="$1" "$ROOT/scripts/build-android.sh" >>"$1" 2>&1 || { echo "cache-hit : build Android en échec, voir $1" >&2; return 1; }
  BENCH_CAPTURE_LOG="$1" "$ROOT/scripts/build-ios.sh" >>"$1" 2>&1 || { echo "cache-hit : build iOS en échec, voir $1" >&2; return 1; }
}

if [ "${BENCH_CACHE_PRIME:-1}" = "1" ]; then
  build_all "$PRIME_LOG" || { tail -30 "$PRIME_LOG" >&2; echo null; exit 0; }
fi
clear_caches
build_all "$LOG" || { tail -30 "$LOG" >&2; echo null; exit 0; }

# Toutes les invocations comptent (Android et iOS), pas seulement la dernière.
# Gradle : "N actionable tasks: … M from cache, …" ; Bazel : "N processes: X remote cache hit, …"
python3 - "$LOG" <<'PY'
import re, sys
text = open(sys.argv[1], errors="replace").read()
hit = tot = lines = 0
for m in re.finditer(r"(\d+) actionable tasks?: ([^\n]*)", text):
    lines += 1; tot += int(m.group(1))
    c = re.search(r"(\d+) from cache", m.group(2)); hit += int(c.group(1)) if c else 0
for m in re.finditer(r"(\d+) process(?:es)?: ([^\n]*)", text):
    lines += 1; tot += int(m.group(1))
    c = re.search(r"(\d+) remote cache hits?", m.group(2)); hit += int(c.group(1)) if c else 0
if not lines or not tot:
    print(f"cache-hit : aucune ligne de synthèse Gradle/Bazel dans {sys.argv[1]}", file=sys.stderr); print("null")
else:
    print(f"cache-hit : {hit}/{tot} tâches ou actions servies par le cache ({lines} invocation(s))", file=sys.stderr)
    if hit == 0:
        print("cache-hit : 0 hit, cache distant absent ou non consulté (voir le log)", file=sys.stderr)
    print(round(100 * hit / tot))
PY
