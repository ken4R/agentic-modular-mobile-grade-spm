#!/usr/bin/env bash
# Approche C : build Android debug via Gradle. Configuration cache et build cache sont activés
# dans gradle.properties / settings.gradle.kts, pas ici, pour que IDE, CI et bench les partagent.
# Sort 0 si OK, laisse les logs sur stdout (bench/cache-hit.sh lit "N actionable tasks: ... M from cache").
set -euo pipefail
cd "$(dirname "$0")/.."
[ -x ./gradlew ] || { echo "gradlew absent : initialiser le wrapper Gradle" >&2; exit 1; }
./gradlew :android:app:assembleDebug --console=plain
