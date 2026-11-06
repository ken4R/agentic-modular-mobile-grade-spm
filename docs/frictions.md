# Journal des frictions — POC C

<<<<<<< HEAD
| Date | Étape du plan | Tokens (contexte utilisé / max) | Coût session | Interventions humaines | Friction |
|---|---|---|---|---|---|
| 2026-11-04 | S1 — ADR 0001 + vérification des versions | 87k / 500k | 0,69 $ | 0 | Environnement cloud : `dl.google.com` refusé par le proxy (403), donc AGP, androidx et SDK Android non téléchargeables. Build Android local impossible, validation via CI uniquement. Pas de Xcode (Linux). |
| 2026-11-04 | S1 — vérification des versions | — | — | 0 | `libs.versions.toml` est en retard (voir ci-dessous). Il appartient au socle : la mise à jour doit se faire dans le template puis passer par `sync-socle.sh`. |
| 2026-11-04 | CI | — | — | 0 | Job `ios` (`macos-15`) jamais attribué à un runner sur `main`, annulé après 15 à 19 min. Sur la branche à 18:57 UTC, un runner a bien été attribué : attente de file plutôt que quota. |
| 2026-11-04 | S1 — étapes 3 à 5 : squelette Gradle, modules KMP, app Android | 180k / 500k | 4,45 $ (cumul session) | 0 | Build Android validé uniquement par la CI (vert du premier coup, 3 min 35). Tests KMP vérifiés en local via un projet JVM jetable (16/16), Maven Central renvoyant des 429 en rafale : 7 essais nécessaires. Catalogue du socle incomplet : artefacts Compose et `activity-compose` ajoutés localement, à reporter dans le template sous peine d'être écrasés par `sync-socle.sh`. |
| 2026-11-04 | S1 — tests | — | — | 0 | `advanceUntilIdle()` n'exécute pas les coroutines de `backgroundScope` : les Stores testés reçoivent un scope sur `StandardTestDispatcher(testScheduler)`. |
| 2026-11-04 | S1 — étape 6 : XCFramework, SKIE, SPM, app SwiftUI | 222k / 500k | 6,63 $ (cumul session) | 0 | SKIE 0.10.0 (socle) ne supporte pas Kotlin 2.2.0 : passage à 0.10.6, à reporter dans le template. Une app iOS ne se build pas avec SPM seul : `.xcodeproj` généré par XcodeGen (`ios/App/project.yml`). Framework statique : `libsqlite3` à lier côté SPM pour le driver SQLDelight natif (1 aller-retour CI). Validation iOS uniquement par la CI. |
| 2026-11-04 | CI | — | — | 0 | Job `ios` à nouveau annulé après 15 min sans runner macOS, puis vert à la relance : attente de file macOS intermittente. |
| 2026-11-04 | S1 — étape 7 : detekt, Konsist, SwiftLint | ~260k / 500k | ~7,5 $ (cumul session) | 0 | `check-architecture.sh` (socle) masque les échecs : `./gradlew detekt -q 2>/dev/null \|\| … \|\| echo "TODO"` sort toujours 0. Un échec detekt ou Konsist n'apparaît que comme « TODO: brancher … » dans le log. À corriger dans le template (retirer les `\|\| echo` de repli). detekt validé en local via le jar CLI (0 issue), Konsist via un projet JVM jetable (3 règles, vérifiées aussi sur des violations volontaires). |
| 2026-11-04 | CI | — | — | 0 | Un runner `macos-15` (Xcode 16.4) n'avait aucun simulateur : `-destination 'platform=iOS Simulator,name=iPhone 16'` échoue. Le build iOS passe à `generic/platform=iOS Simulator`, qui ne dépend pas des simulateurs installés. |

## Versions stables relevées sur Maven Central le 2026-11-04

| Clé | Catalogue | Dernière stable |
|---|---|---|
| kotlin | 2.2.0 | 2.4.21 |
| compose-multiplatform | 1.8.0 | 1.12.2 |
| ktor | 3.1.0 | 3.6.1 |
| sqldelight | 2.1.0 | 2.4.1 |
| koin | 4.1.0 | 4.2.2 |
| coroutines | 1.10.0 | 1.11.0 |
| skie | 0.10.0 | 0.10.16 |
| konsist | 0.17.0 | 0.18.1 |
| detekt | 1.23.8 | 1.23.8 (Gradle Plugin Portal) |
| mvikotlin | 4.3.0 | 4.4.0 |
| agp, compose-bom | 8.9.0, 2025.06.00 | non vérifiable (Google Maven bloqué) |

La compatibilité SKIE ↔ Kotlin n'est pas vérifiée : à confirmer dans les notes de version de SKIE avant de monter Kotlin.
=======
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
>>>>>>> origin/main
