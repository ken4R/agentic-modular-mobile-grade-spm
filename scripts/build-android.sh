#!/usr/bin/env bash
<<<<<<< HEAD
# Approche C : Gradle pour tout le Kotlin (voir CLAUDE.md).
# Doit : builder l'app android en debug, sortir 0 si OK, laisser les logs sur stdout (lus par bench/cache-hit.sh).
set -euo pipefail
cd "$(dirname "$0")/.."
./gradlew :android:app:assembleDebug
=======
# Approche C : build Android debug via Gradle. Configuration cache et build cache sont activés
# dans gradle.properties / settings.gradle.kts, pas ici, pour que IDE, CI et bench les partagent.
# Sort 0 si OK, laisse les logs sur stdout (bench/cache-hit.sh lit "N actionable tasks: ... M from cache").
set -euo pipefail
cd "$(dirname "$0")/.."
[ -x ./gradlew ] || { echo "gradlew absent : initialiser le wrapper Gradle" >&2; exit 1; }
./gradlew :android:app:assembleDebug --console=plain
>>>>>>> origin/main
