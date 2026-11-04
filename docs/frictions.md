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

- **SKIE / sealed interface (ouvert)** : `onEnum(of:)` introuvable pour `AuthIntent` (sealed interface d'un module
  de feature exporté), alors qu'il fonctionne pour `Outcome` (sealed class de core). 2 échecs consécutifs
  (tableau d'existentiels, puis fonction générique typée concrètement) → arrêt selon AGENTS.md.
  Hypothèse non vérifiée : SKIE ne génère pas l'interop sealed pour ce cas ; le Swift généré (`shared/build/`)
  n'est pas lisible par l'agent. Option C retenue par l'humain : contrat limité au `StateFlow`, `switch`
  exhaustif en attente. À reprendre : A (`sealed class`) ou B (diagnostic sur le Swift généré).
- **check-architecture.sh (template)** : 2 violations detekt réelles affichées « TODO: brancher detekt », sortie 0.
- **Tests UI Android** : instrumentés, exécutés sur un émulateur headless démarré par l'agent (AVD API 36).
- **Interventions humaines** : 3 au total (gradle.properties, consigne d'autonomie, choix de l'option C).
