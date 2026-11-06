#!/usr/bin/env bash
# Tests unitaires KMP (commonTest). À adapter par approche (Gradle en A/C, bazel test en B).
# Approche C : `allTests` sur tous les modules ; `:shared` est le parapluie iOS, sans tests.
# TEST_SHARED_CMD est volontairement non quoté : c'est une commande avec ses arguments.
set -euo pipefail
cd "$(dirname "$0")/.."
# shellcheck disable=SC2086
${TEST_SHARED_CMD:-./gradlew allTests -q} 2>&1 | tail -60
