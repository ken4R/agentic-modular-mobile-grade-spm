#!/usr/bin/env bash
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
