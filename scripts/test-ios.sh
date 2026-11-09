#!/usr/bin/env bash
# Approche C : exécute les tests Swift des packages SPM (ios/Core, ios/Features/*) sur un simulateur.
# Les packages sont iOS-only et Shared.xcframework n'a que la slice iosSimulatorArm64 : `swift test` (macOS)
# est impossible, on passe par `xcodebuild test` sur un simulateur arm64 réel (pas de destination générique).
# IOS_DESTINATION force la destination ; sinon le premier iPhone disponible du runner est choisi.
# DERIVED_DATA_PATH : DerivedData dans un dossier choisi (un sous-dossier par package), mis en cache par la CI.
set -euo pipefail
cd "$(dirname "$0")/.."
command -v xcodebuild >/dev/null || { echo "xcodebuild absent : tests iOS impossibles sur cette machine" >&2; exit 1; }
[ -x ./gradlew ] || { echo "gradlew absent : initialiser le wrapper Gradle" >&2; exit 1; }

./gradlew :shared:assembleSharedDebugXCFramework --console=plain -q

if [ -n "${IOS_DESTINATION:-}" ]; then
  DESTINATION="$IOS_DESTINATION"
else
  UDID=$(xcrun simctl list devices available -j \
    | python3 -c 'import json,sys
d=json.load(sys.stdin)["devices"]
# Tri numérique sur la version du runtime (…SimRuntime.iOS-18-10 > iOS-18-6), plus récent en premier.
ver=lambda r: tuple(int(p) for p in r.rsplit("iOS-",1)[1].split("-") if p.isdigit())
ids=[x["udid"] for r,l in sorted((i for i in d.items() if "iOS-" in i[0]),key=lambda i: ver(i[0]),reverse=True) for x in l if x["name"].startswith("iPhone")]
print(ids[0] if ids else "")')
  [ -n "$UDID" ] || { echo "aucun simulateur iPhone disponible" >&2; xcrun simctl list devices available; exit 1; }
  DESTINATION="id=$UDID"
fi
echo "Destination : $DESTINATION"

# Schéma auto-généré par Xcode pour un package : « <Package>-Package » s'il existe, sinon le schéma du produit.
scheme_for() {
  xcodebuild -list -json 2>/dev/null | python3 -c 'import json,sys
s=json.load(sys.stdin)["workspace"]["schemes"]
p=[x for x in s if x.endswith("-Package")]
print((p or s)[0])'
}

LOG_DIR="${RUNNER_TEMP:-/tmp}/ios-tests"
mkdir -p "$LOG_DIR"
status=0
for pkg in ios/Core ios/Features/*/; do
  pkg=${pkg%/}
  scheme=$(cd "$pkg" && scheme_for)
  log="$LOG_DIR/$(basename "$pkg").log"
  echo "--- $pkg (schéma $scheme, log complet : $log) ---"
  rc=0
  DD_ARGS=()
  [ -n "${DERIVED_DATA_PATH:-}" ] && DD_ARGS=(-derivedDataPath "$DERIVED_DATA_PATH/$(basename "$pkg")")
  (cd "$pkg" && xcodebuild test -scheme "$scheme" -destination "$DESTINATION" ${DD_ARGS[@]+"${DD_ARGS[@]}"} ARCHS=arm64) \
    >"$log" 2>&1 || rc=$?
  grep -E "error:|Test Case .* (passed|failed)|Executed [0-9]+ test|\*\* TEST" "$log" | head -80 || true
  [ "$rc" -eq 0 ] || { echo "ÉCHEC $pkg (exit $rc)"; tail -30 "$log"; status=1; }
done
exit $status
