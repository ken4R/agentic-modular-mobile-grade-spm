#!/usr/bin/env bash
# Approche C : Gradle pour tout le Kotlin (voir CLAUDE.md).
# Doit : builder l'app android en debug, sortir 0 si OK, laisser les logs sur stdout (lus par bench/cache-hit.sh).
set -euo pipefail
cd "$(dirname "$0")/.."
./gradlew :android:app:assembleDebug
