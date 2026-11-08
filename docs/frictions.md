# Journal des frictions — POC C

## 2026-11-04 — Semaine 1, étapes 0-1

- **Versions (template)** : SKIE 0.10.0 ne supporte pas Kotlin 2.2.0 (support à partir de 0.10.4).
  Kotlin 2.2.0 est validé avec Xcode 16.3, la machine locale a Xcode 26.2. Correction à faire dans
  `libs.versions.toml` du template, puis `sync-socle.sh`.
- **Gradle** : wrapper 8.14.3, seule plage commune AGP 8.9 (≥ 8.11.1) et Kotlin 2.2.0 (≤ 8.14).
- **Nom du parapluie** : CLAUDE.md utilise `:shared`, la skill `new-feature-module` (référence C)
  utilise `shared/umbrella`. Retenu : `:shared`. Conséquence : `:shared:allTests` ne couvre pas les features.
- **check-architecture.sh (template)** : la chaîne `|| … || echo TODO` masque un échec réel de detekt/Konsist.
- **bench/_lib.sh (template)** : `clear_caches` ne vide pas `android/**/build`, le build à froid Android est biaisé.
- **Simulateur** : pas d'« iPhone 16 » en local, `IOS_DESTINATION` requis.
- **Sandbox agent** : Gradle (`~/.gradle`) et CoreSimulator ne fonctionnent pas sous sandbox ;
  `gradle.properties` est en refus de lecture/écriture pour l'agent, créé par l'humain (1 intervention).

## 2026-11-04 — Semaine 1, étapes 2-4

- **Décision autonome (catalogue)** : les entrées manquantes de `libs.versions.toml` sont ajoutées dans ce POC
  et listées ici pour remontée au template (sinon `sync-socle.sh` les écrasera).
- **detekt** : le plugin n'analyse pas les source sets KMP par défaut ; `source = src/` dans la convention.
- **Konsist** : la tâche de test lit tout le dépôt ; sans entrées déclarées elle resterait à jour (ou servie
  par le build cache) après une violation. Vérifié par mutation (import interdit dans `core` → échec).
- **check-architecture.sh (template)** : sans `ios/`, SwiftLint échoue (« No lintable files ») et le script
  affiche « swiftlint absent » en sortant 0.
- **Sandbox agent** : lecture de `build/` refusée, les rapports de tests ne sont consultables que via la sortie Gradle.

## 2026-11-04 — Semaine 1, étape 5

- **CLAUDE.md** : `:shared:assembleXCFramework` n'existe pas (tâche réelle : `assembleSharedXCFramework`,
  dérivée du nom du XCFramework) et `:shared:allTests` ne couvre que le parapluie. Commandes corrigées.
- **SKIE** : 0.10.6 retenu (Kotlin 2.2.0 et 2.2.10) ; chargé par la convention `kmp-umbrella` avec le plugin Kotlin
  pour partager le classloader. XCFramework debug OK avec Kotlin 2.2.0 + Xcode 26.2, malgré la compatibilité
  officielle limitée à Xcode 16.3.
- **expect/actual classes** en bêta (avertissement) : remplacées par une interface + implémentations par plateforme.
- **Framework statique** : l'app iOS devra lier `libsqlite3` (driver natif SQLDelight).

## 2026-11-04 — Semaine 1, étapes 6-7

- **App iOS** : SPM ne produit pas d'app iOS ; `App` est une cible Xcode générée par XcodeGen
  (`ios/project.yml`), `App.xcodeproj` commité car la CI commune n'installe pas XcodeGen. Écart avec le plan
  (« package SPM App ») ; à revoir lors du fork POC A (Bazel / rules_xcodeproj).
- **binaryTarget** hors de la racine du package (`../../shared/build/...`) : accepté par SPM (Xcode 26.2).
- **DI iOS** : Koin est démarré par `IosGraph.start()` (Kotlin, parapluie) ; les modules des features s'y ajoutent,
  et non dans le code Swift de `ios/App` comme le décrit la skill `new-feature-module`.
- **Simulateur** : `name=iPhone 17 Pro Max` refusé une fois par xcodebuild alors que listé ; `id=<UDID>` fiable.
- **check-architecture.sh (template)** : 2 violations SwiftLint `--strict` → script en succès (« swiftlint absent »).
- **Sandbox agent** : XcodeGen et le cache SwiftLint écrivent hors du dépôt, exécution hors sandbox nécessaire.

## 2026-11-04 — Semaine 1, étape 8 (feature auth)

- **SKIE / sealed interface (résolu le 2026-11-07, voir plus bas)** : `onEnum(of:)` introuvable pour `AuthIntent` (sealed interface d'un module
  de feature exporté), alors qu'il fonctionne pour `Outcome` (sealed class de core). 2 échecs consécutifs
  (tableau d'existentiels, puis fonction générique typée concrètement) → arrêt selon AGENTS.md.
  Hypothèse non vérifiée : SKIE ne génère pas l'interop sealed pour ce cas ; le Swift généré (`shared/build/`)
  n'est pas lisible par l'agent. Option C retenue par l'humain : contrat limité au `StateFlow`, `switch`
  exhaustif en attente. À reprendre : A (`sealed class`) ou B (diagnostic sur le Swift généré).
- **check-architecture.sh (template)** : 2 violations detekt réelles affichées « TODO: brancher detekt », sortie 0.
- **Tests UI Android** : instrumentés, exécutés sur un émulateur headless démarré par l'agent (AVD API 36).
- **Interventions humaines** : 3 au total (gradle.properties, consigne d'autonomie, choix de l'option C).

## 2026-11-06 — CI après synchronisation du socle

- **test-shared.sh (template)** : `"${TEST_SHARED_CMD:-./gradlew :shared:allTests -q}"` est quoté, bash cherche un
  exécutable nommé `./gradlew :shared:allTests -q` (exit 127). Et `:shared` est le parapluie, sans tests.
  Corrigé ici en `${TEST_SHARED_CMD:-./gradlew allTests -q}` ; à reporter dans le template.
- **check-architecture.sh (template)** : `grep -vE '^$'` sort 1 quand detekt ou Konsist réussissent en `-q`
  (aucune sortie), donc `pipefail` fait échouer un check vert. Remplacé par `sed '/^$/d'`.
- **check-architecture.sh (template)** : le script tourne dans le job `android` (Linux), où SwiftLint n'existe pas ;
  avec `CI=true` il échouait toujours. SwiftLint est ignoré sur Linux et lancé par `build-ios.sh` sur le runner
  macOS (installé par `setup-ios-tools.sh`). Correction à reporter dans le template (étape SwiftLint dans le job `ios`).
- **Runner macOS** : le job `ios` de `main` (run 24, 2026-11-06) est resté sans runner puis annulé après 15 min ;
  celui de `copie-main` (run 23, même jour) a été vert. Relancer avant de conclure à un échec.
- **Simulateur en CI** : run 42299231316, le runner macos-15 (Xcode 16.4) n'avait aucun simulateur « iPhone 16 »
  (exit 70). En CI, `build-ios.sh` vise `generic/platform=iOS Simulator` avec `ARCHS=arm64` (seule slice du XCFramework).

## 2026-11-07 — Semaine 1, features catalog et cart (agent cloud)

- **Mesure « ajout d'une feature (agent) »** : catalog 3 min 23 s, cart 1 min 05 s d'écriture
  (`bench/feature-timings.log`), après ~4 min de lecture (AGENTS.md, skill, feature auth, référence a13a2ef).
  Les horodatages `end` sont pris avant la validation : l'environnement cloud ne peut ni compiler
  (AGP indisponible, dl.google.com refusé par le proxy) ni lancer Xcode. La CI GitHub fait foi.
- **Tokens** : `/cost` indisponible dans une session cloud ; estimation ~110 k tokens de contexte pour les deux features.
- **Interventions humaines** : 0 (lancement validé dans le fil du plan).
- **Skill new-feature-module** : ne dit pas comment une feature déclenche l'action d'une autre (ajout au panier
  depuis le catalogue). Retenu : la vue catalog reçoit `onAddToCart` de l'app, qui l'envoie au `CartStore` ;
  catalog ne dépend pas de cart, cart ne dépend que de catalog-api. Le Store partagé est détenu par l'app
  (voir `docs/adr/0003-actions-inter-features.md`).
- **Skill new-feature-module** : l'étape « ajouter le package à l'App » ne couvre pas `App.xcodeproj` commité
  (CI sans XcodeGen) : les entrées pbxproj (6 sections) ont été écrites à la main, en miroir de `project.yml`.
- **Tests UI** : Android instrumentés et `xcodebuild test` des packages non exécutés (ni émulateur, ni CI pour eux) ;
  écrits sur le modèle d'auth, à lancer en local.

## 2026-11-07 — Semaine 1, point SKIE onEnum (agent cloud, PR #5)

- **SKIE / sealed interface (résolu)** : option B. SKIE 0.10.6 génère bien `onEnum(of:)` pour les sealed interfaces
  des modules de feature (`AuthIntent`, `CatalogIntent`, `CartIntent`, signatures relevées dans les logs du job `ios`).
  `IntentContractTests` fait un `switch` exhaustif sur les trois et compile (`TEST BUILD SUCCEEDED`, Xcode 16.4).
  Cause de l'échec de l'étape 8 non reproduite : probablement l'appel côté test, pas SKIE. Option A (`sealed class`) inutile.
- **Tests de contrat en CI** : `build-ios.sh` compile `ios/Core` en `build-for-testing` (CI seulement, destination
  générique) : l'exhaustivité est vérifiée, les tests ne sont pas exécutés (pas de simulateur garanti). ~+10 s sur le job.
- **Tokens agent** : ~60k tokens de contexte pour l'étape (lecture frictions/AGENTS.md, test, script, PR, logs CI).

## 2026-11-08 — Tests UI des deux plateformes en CI (agent cloud)

- **Constat** : `ci.yml` ne lançait ni `connectedAndroidTest` (aucun émulateur sur le job `android`) ni
  `xcodebuild test` (le job `ios` fait `build` + `build-for-testing` de `ios/Core` seulement, destination générique).
  Les tests `AuthUITests`/`CatalogUITests`/`CartUITests`, `ContractTests` et les `androidTest` n'étaient jamais exécutés.
- **Choix** : workflow séparé `.github/workflows/tests.yml` pour ne pas toucher `ci.yml` (commun aux POC, métrique
  « durée pipeline PR » comparable). Jobs `android-instrumented` (reactivecircus/android-emulator-runner, API 34 x86_64,
  KVM) et `ios-tests` (macos-15, `scripts/test-ios.sh`). `swift test` impossible : packages iOS-only et XCFramework
  limité à `iosSimulatorArm64`, donc `xcodebuild test` sur le premier iPhone simulé disponible (UDID, pas de nom codé en dur).
- **Validation** : impossible en local (ni SDK Android, ni Xcode) ; la CI GitHub fait foi. Run de la PR #6 :
  `android-instrumented` 6 tests passés (2 par feature) en ~3,5 min ; `ios-tests` 12 tests passés (Core 7, Auth 2,
  Cart 2, Catalog 1) en ~14 min, dont ~2 à 4 min de compilation par package (sans cache DerivedData).
- **Tokens agent** : non relevé à l'étape ; ~45k tokens de contexte pour la relecture indépendante (diff, logs CI).

## 2026-11-08 — Semaine 2, optimisations Gradle/Xcode (agent cloud)

- **Validation locale impossible** : `./gradlew help` échoue (Maven Central en 429 sur trove4j, Google Maven
  bloqué). Tout est validé par la CI GitHub : `tests/impact`, `perf/gradle-caches`, `bench/grille`.
- **`gradle-dependency-graph`** : le plugin vanniktech (0.8.0, juin 2022) n'est plus maintenu et dessine en
  Graphviz. Remplacé par une tâche `projectGraph` dans `build-logic` (ADR-0004).
- **`ci.yml` intouchable** : le cache DerivedData et le cache distant ne peuvent pas profiter au job `ios` de
  `ci.yml` sans changer le template (étape `actions/cache` et variables `GRADLE_REMOTE_CACHE_*`). Appliqué
  aux workflows du POC (`tests.yml`, `perf.yml`). À reporter au template si la mesure le justifie.
- **DerivedData et mtimes** : un checkout neuf donne de nouvelles dates à tous les fichiers ; Xcode recompile
  donc les sources Swift même avec DerivedData restauré. Le gain attendu vient surtout du ModuleCache et de la
  résolution SPM. `Shared.xcframework` est de toute façon reconstruit par Gradle. À mesurer sur la durée du job.
- **Kotlin/Native** : `~/.konan` (toolchain et caches K/N) n'est pas mis en cache par `setup-gradle` ; ajouté
  au job `ios-tests`.
- **Bench et cache distant** : `run-all.sh` enchaîne builds à froid et ratio de cache avec le même environnement.
  Un cache distant actif rendrait les runs 2 et 3 « à froid » chauds. `bench.yml` lance donc `run-all.sh` sans
  cache distant, puis remesure `cache-hit.sh` avec le cache distant rempli (`cache_hit_pct_local_only` garde la
  valeur d'origine).
- **`bench/results-*.json` ignoré par git** (`.gitignore` du socle) alors que la skill `run-benchmarks` demande de
  le commiter : à trancher dans le template.
- **Tokens** : ~85k tokens de contexte pour l'étape (lecture plan/AGENTS.md/scripts, écriture, PR) ;
  `/cost` indisponible en session cloud. Interventions humaines : 0.
- **Configuration cache et AGP sur runner neuf** : le 2e build d'un même job ne réutilise pas le configuration cache
  (« file system entry '~/.config/.android/cache' has been created ») : AGP teste ce dossier à la configuration et le
  crée au 1er build. Touche aussi `ci.yml` (build Android puis tests KMP dans le même job). `check-gradle-caches.sh`
  crée le dossier d'avance ; à reporter au template (étape ou script commun) si le gain est mesuré.

## 2026-11-08 — Suivi de la relecture de `tests.yml` (agent cloud)

- **Runtime simulateur** : `test-ios.sh` triait les runtimes par ordre lexicographique (`iOS-18-6` avant
  `iOS-18-10`) ; tri numérique sur la version.
- **Logs iOS** : les logs `xcodebuild` complets (`$RUNNER_TEMP/ios-tests/`) sont uploadés en artefact en cas d'échec,
  comme les rapports Android.
- **`cancel-in-progress`** : réservé aux PR, pour que chaque push sur `main` garde son run.
- **Validation** : impossible en local (ni Xcode ni SDK Android) ; tri vérifié en Python sur un JSON `simctl` factice.
