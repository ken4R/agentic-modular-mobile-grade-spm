#!/usr/bin/env bash
<<<<<<< HEAD
# Approche C : Gradle produit Shared.xcframework, SPM + Xcode natif pour le Swift (voir CLAUDE.md).
# Doit : builder l'app ios en debug, sortir 0 si OK, laisser les logs sur stdout (lus par bench/cache-hit.sh).
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew :shared:assembleSharedDebugXCFramework
rm -rf ios/Core/Shared.xcframework
cp -R shared/build/XCFrameworks/debug/Shared.xcframework ios/Core/

command -v xcodegen >/dev/null || brew install xcodegen
(cd ios/App && xcodegen generate)

# Build seul : destination générique, qui ne dépend pas des simulateurs installés sur le runner
# (un runner macos-15 sans simulateur "iPhone 16" a fait échouer la CI). Les tests UI choisiront un appareil.
# ARCHS=arm64 : la destination générique builde aussi x86_64, slice absente de Shared.xcframework (iosSimulatorArm64 seul).
cd ios/App
xcodebuild -scheme App \
  -destination 'generic/platform=iOS Simulator' \
  ARCHS=arm64 \
  -derivedDataPath ../../build/DerivedData \
  CODE_SIGNING_ALLOWED=NO \
  build
=======
# Approche C : Shared.xcframework (debug) via Gradle, puis app SwiftUI via xcodebuild (SPM natif).
# Gradle est toujours relancé : un changement KMP doit invalider le XCFramework (bench/incremental.sh).
# Sort 0 si OK, laisse les logs sur stdout (bench/cache-hit.sh lit "N actionable tasks: ... M from cache").
# IOS_DESTINATION permet de cibler un autre simulateur que celui de CLAUDE.md (absent en local).
set -euo pipefail
cd "$(dirname "$0")/.."
DESTINATION="${IOS_DESTINATION:-platform=iOS Simulator,name=iPhone 16}"
command -v xcodebuild >/dev/null || { echo "xcodebuild absent : build iOS impossible sur cette machine" >&2; exit 1; }
[ -x ./gradlew ] || { echo "gradlew absent : initialiser le wrapper Gradle" >&2; exit 1; }

./gradlew :shared:assembleSharedDebugXCFramework --console=plain
# Projet généré par XcodeGen depuis ios/project.yml.
xcodebuild -project ios/App.xcodeproj -scheme App -configuration Debug -destination "$DESTINATION" build
>>>>>>> origin/main
