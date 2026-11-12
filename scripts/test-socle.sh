#!/usr/bin/env bash
# Auto-test du socle, sans app : syntaxe des scripts, puis bench/cache-hit.sh sur des builds factices.
# Lancé par le job `socle` de ci.yml (le template n'a pas d'app à compiler).
set -euo pipefail
cd "$(dirname "$0")/.."
for f in scripts/*.sh scripts/bench/*.sh bootstrap.sh; do [ -f "$f" ] && bash -n "$f"; done
echo "syntaxe : OK"

W="$(mktemp -d)"; trap 'rm -rf "$W"' EXIT
mkdir -p "$W/scripts/bench" "$W/shared/core/build" "$W/android/app/build" "$W/home"
cp scripts/bench/*.sh "$W/scripts/bench/"
printf '#!/usr/bin/env bash\nexit 0\n' >"$W/gradlew"
fake() {  # fake <script> <sortie> [code]
  printf '#!/usr/bin/env bash\necho "%s"\nexit %s\n' "$2" "${3:-0}" >"$W/scripts/$1"; chmod +x "$W/scripts/$1" "$W/gradlew"
}
check() {  # check <attendu> <libellé> : lance cache-hit.sh dans le bac à sable
  local got; got=$(HOME="$W/home" bash "$W/scripts/bench/cache-hit.sh" 2>/dev/null)
  [ "$got" = "$1" ] || { echo "ÉCHEC cache-hit ($2) : attendu $1, obtenu $got" >&2; exit 1; }
  echo "cache-hit ($2) : $got"
}
fake build-android.sh "50 actionable tasks: 10 executed, 30 from cache, 10 up-to-date"
fake build-ios.sh "20 actionable tasks: 20 executed"
check 43 "Android + iOS sommés"
[ ! -e "$W/android/app/build" ] && [ ! -e "$W/shared/core/build" ] || { echo "ÉCHEC : clear_caches n'a pas purgé android/ ou shared/" >&2; exit 1; }
fake build-ios.sh "boom" 3
check null "build en échec"
fake build-android.sh "rien"; fake build-ios.sh "rien"
check null "aucune synthèse"
echo "== socle OK"
