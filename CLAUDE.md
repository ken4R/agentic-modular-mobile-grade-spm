Lis `AGENTS.md` avant toute action.

## Approche C — Gradle + SPM, sans Bazel (baseline)

- **Gradle** pour tout le Kotlin, **SPM** pour tout le Swift, Xcode natif. Aucun Bazel.
- Optimisations à activer et conserver : configuration cache Gradle, build cache distant
  (`gradle.properties` + `settings.gradle.kts`), build par impact via `gradle-dependency-graph` en CI,
  cache DerivedData Xcode en CI.
- Les `Package.swift` sont écrits à la main, `binaryTarget` vers `Shared.xcframework`.

### Commandes
- Android : `./gradlew :android:app:assembleDebug`
- Tests KMP : `./gradlew allTests` (tous les modules ; `:shared` est le parapluie iOS, sans tests)
- XCFramework : `./gradlew :shared:assembleSharedDebugXCFramework` (debug, utilisé par `build-ios.sh`) ou `:shared:assembleSharedXCFramework` (debug + release)
- iOS : `xcodebuild -scheme App -destination 'platform=iOS Simulator,name=iPhone 16' build`
- Modules impactés : `./scripts/affected-modules.sh [--base origin/main] [--format list|tasks|github]`
- Caches Gradle (configuration cache + cache distant) : `./scripts/check-gradle-caches.sh`
- Cache distant : variables `GRADLE_REMOTE_CACHE_URL`, `_USERNAME`, `_PASSWORD` (`settings.gradle.kts`)

### Interdits
- Ne jamais créer de fichier `BUILD`, `BUILD.bazel`, `MODULE.bazel`, `WORKSPACE` ou `.bzl`.
- Ne jamais proposer Bazel comme solution à un problème de build : documenter la friction dans `docs/frictions.md` à la place.
