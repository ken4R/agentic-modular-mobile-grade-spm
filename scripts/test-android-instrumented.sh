#!/usr/bin/env bash
# Approche C : tests UI Compose instrumentés (android/features/*/src/androidTest) sur un émulateur ou appareil
# déjà démarré (adb). En CI, l'émulateur est fourni par reactivecircus/android-emulator-runner (tests.yml).
# Arguments facultatifs : tâches à lancer (build par impact, ex. `:android:features:cart:connectedDebugAndroidTest`).
set -euo pipefail
cd "$(dirname "$0")/.."
command -v adb >/dev/null || { echo "adb absent : aucun SDK Android sur cette machine" >&2; exit 1; }
[ -n "$(adb devices | sed 1d | grep -w device)" ] || { echo "aucun émulateur/appareil connecté (adb devices)" >&2; exit 1; }
if [ $# -eq 0 ]; then set -- connectedDebugAndroidTest; fi
./gradlew "$@" --console=plain
