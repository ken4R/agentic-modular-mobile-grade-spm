# Journal des frictions — POC C

| Date | Étape du plan | Tokens (contexte utilisé / max) | Coût session | Interventions humaines | Friction |
|---|---|---|---|---|---|
| 2026-11-04 | S1 — ADR 0001 + vérification des versions | 87k / 500k | 0,69 $ | 0 | Environnement cloud : `dl.google.com` refusé par le proxy (403), donc AGP, androidx et SDK Android non téléchargeables. Build Android local impossible, validation via CI uniquement. Pas de Xcode (Linux). |
| 2026-11-04 | S1 — vérification des versions | — | — | 0 | `libs.versions.toml` est en retard (voir ci-dessous). Il appartient au socle : la mise à jour doit se faire dans le template puis passer par `sync-socle.sh`. |
| 2026-11-04 | CI | — | — | 0 | Job `ios` (`macos-15`) jamais attribué à un runner sur `main`, annulé après 15 à 19 min. Sur la branche à 18:57 UTC, un runner a bien été attribué : attente de file plutôt que quota. |
| 2026-11-04 | S1 — étapes 3 à 5 : squelette Gradle, modules KMP, app Android | 180k / 500k | 4,45 $ (cumul session) | 0 | Build Android validé uniquement par la CI (vert du premier coup, 3 min 35). Tests KMP vérifiés en local via un projet JVM jetable (16/16), Maven Central renvoyant des 429 en rafale : 7 essais nécessaires. Catalogue du socle incomplet : artefacts Compose et `activity-compose` ajoutés localement, à reporter dans le template sous peine d'être écrasés par `sync-socle.sh`. |
| 2026-11-04 | S1 — tests | — | — | 0 | `advanceUntilIdle()` n'exécute pas les coroutines de `backgroundScope` : les Stores testés reçoivent un scope sur `StandardTestDispatcher(testScheduler)`. |
| 2026-11-04 | S1 — étape 6 : XCFramework, SKIE, SPM, app SwiftUI | 222k / 500k | 6,63 $ (cumul session) | 0 | SKIE 0.10.0 (socle) ne supporte pas Kotlin 2.2.0 : passage à 0.10.6, à reporter dans le template. Une app iOS ne se build pas avec SPM seul : `.xcodeproj` généré par XcodeGen (`ios/App/project.yml`). Framework statique : `libsqlite3` à lier côté SPM pour le driver SQLDelight natif (1 aller-retour CI). Validation iOS uniquement par la CI. |
| 2026-11-04 | CI | — | — | 0 | Job `ios` à nouveau annulé après 15 min sans runner macOS, puis vert à la relance : attente de file macOS intermittente. |

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
