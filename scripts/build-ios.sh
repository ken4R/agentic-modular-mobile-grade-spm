#!/usr/bin/env bash
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
cd ios/App
xcodebuild -scheme App \
  -destination 'generic/platform=iOS Simulator' \
  -derivedDataPath ../../build/DerivedData \
  CODE_SIGNING_ALLOWED=NO \
  build
