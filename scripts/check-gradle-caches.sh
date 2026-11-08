#!/usr/bin/env bash
# Semaine 2 (approche C) : valide le configuration cache et le cache de build distant HTTP.
#   1. build complet : entrée de configuration cache stockée, sorties poussées vers le cache distant ;
#   2. même build : entrée de configuration cache réutilisée ;
#   3. sorties et cache local supprimés : les tâches reviennent du cache distant (ratio affiché).
# Le cache distant est le serveur local scripts/http-build-cache.py, sauf si GRADLE_REMOTE_CACHE_URL est fourni.
# CHECK_TASKS : tâches à valider (défaut : tous les modules Android/KMP, tests et detekt compris).
set -euo pipefail
cd "$(dirname "$0")/.."
read -r -a TASKS <<<"${CHECK_TASKS:-:android:app:assembleDebug assembleDebugAndroidTest allTests detekt}"
WORK="${RUNNER_TEMP:-$(mktemp -d)}/gradle-caches-check"; mkdir -p "$WORK"

SERVER=""
if [ -z "${GRADLE_REMOTE_CACHE_URL:-}" ]; then
  PORT="${CACHE_PORT:-5071}"
  python3 scripts/http-build-cache.py "$PORT" "$WORK/http-cache" >"$WORK/server.log" 2>&1 &
  SERVER=$!
  trap 'kill "$SERVER" 2>/dev/null || true' EXIT
  for _ in $(seq 1 50); do curl -fs "http://127.0.0.1:$PORT/stats" >/dev/null && break; sleep 0.2; done
  export GRADLE_REMOTE_CACHE_URL="http://127.0.0.1:$PORT/cache/"
fi
export GRADLE_REMOTE_CACHE_PUSH=true

run() {  # run <n> : lance le build, garde le log, s'arrête au premier échec
  local log="$WORK/run-$1.log"
  if ! ./gradlew "${TASKS[@]}" --console=plain >"$log" 2>&1; then
    echo "échec du build $1 :" >&2; grep -E "FAILED|What went wrong|problem" -A5 "$log" | head -60 >&2; exit 1
  fi
  grep -E "Configuration cache|actionable tasks" "$log" || true
}
expect() {  # expect <n> <motif> <message>
  grep -qE "$2" "$WORK/run-$1.log" || { echo "ÉCHEC : $3" >&2; exit 1; }
}

echo "== 1. build initial"
run 1; expect 1 "Configuration cache entry stored" "entrée de configuration cache non stockée"
echo "== 2. même build"
run 2; expect 2 "Configuration cache entry reused" "configuration cache non réutilisé"
echo "== 3. sorties et cache local supprimés"
rm -rf "${GRADLE_USER_HOME:-$HOME/.gradle}/caches/build-cache-1"
find shared android -type d -name build -prune -exec rm -rf {} +
run 3; expect 3 "Configuration cache entry reused" "configuration cache non réutilisé après purge des sorties"
LINE=$(grep -oE '[0-9]+ actionable tasks: .*' "$WORK/run-3.log" | tail -1)
TOTAL=$(echo "$LINE" | grep -oE '^[0-9]+'); FROM=$(echo "$LINE" | grep -oE '[0-9]+ from cache' | grep -oE '^[0-9]+' || echo 0)
echo "cache distant : $FROM / $TOTAL tâches depuis le cache ($((100 * FROM / TOTAL)) %)"
[ -n "$SERVER" ] && echo "serveur : $(curl -fs "${GRADLE_REMOTE_CACHE_URL%cache/}stats")"
[ "$FROM" -gt 0 ] || { echo "ÉCHEC : aucune tâche servie par le cache distant" >&2; exit 1; }
echo "== OK"
