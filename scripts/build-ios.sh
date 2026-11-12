#!/usr/bin/env bash
# Approche C : Shared.xcframework (debug) via Gradle, puis app SwiftUI via xcodebuild (SPM natif).
# Gradle est toujours relancé : un changement KMP doit invalider le XCFramework (bench/incremental.sh).
# Sort 0 si OK, laisse les logs sur stdout (bench/cache-hit.sh lit "N actionable tasks: ... M from cache").
# IOS_DESTINATION permet de cibler un autre simulateur que celui de CLAUDE.md (absent en local).
set -euo pipefail
cd "$(dirname "$0")/.."
# En CI, destination générique : certains runners macos-15 n'ont aucun simulateur « iPhone 16 ».
# ARCHS=arm64 car Shared.xcframework n'a que la slice iosSimulatorArm64 (pas de x86_64).
if [ "${CI:-false}" = "true" ]; then
  DESTINATION="${IOS_DESTINATION:-generic/platform=iOS Simulator}"
  EXTRA_ARGS=(ARCHS=arm64)
else
  DESTINATION="${IOS_DESTINATION:-platform=iOS Simulator,name=iPhone 16}"
  EXTRA_ARGS=()
fi
command -v xcodebuild >/dev/null || { echo "xcodebuild absent : build iOS impossible sur cette machine" >&2; exit 1; }
[ -x ./gradlew ] || { echo "gradlew absent : initialiser le wrapper Gradle" >&2; exit 1; }

./gradlew :shared:assembleSharedDebugXCFramework --console=plain
# Diagnostic SKIE (docs/frictions.md, onEnum) : signatures `onEnum(of:)` générées, lisibles dans les logs CI.
echo "--- SKIE : onEnum générés ---"
grep -rhoE "func onEnum<[^(]*\(of sealed: [^)]*\)" shared/build/skie --include='*.swift' 2>/dev/null | sort -u | head -40 || true
# Projet généré par XcodeGen depuis ios/project.yml.
xcodebuild -project ios/App.xcodeproj -scheme App -configuration Debug -destination "$DESTINATION" ${EXTRA_ARGS[@]+"${EXTRA_ARGS[@]}"} build

# Contrats SKIE (ios/Core/Tests/ContractTests) : compilés en CI, le `switch` exhaustif sur onEnum(of:) est
# vérifié à la compilation. Pas d'exécution : la destination CI est générique (aucun simulateur garanti).
if [ "${CI:-false}" = "true" ]; then
  (cd ios/Core && xcodebuild -scheme Core -destination "$DESTINATION" ${EXTRA_ARGS[@]+"${EXTRA_ARGS[@]}"} build-for-testing)
fi
