# Journal des frictions — POC C

| Date | Étape du plan | Tokens (contexte utilisé / max) | Coût session | Interventions humaines | Friction |
|---|---|---|---|---|---|
| 2026-11-04 | S1 — ADR 0001 + vérification des versions | 87k / 500k | 0,69 $ | 0 | Environnement cloud : `dl.google.com` refusé par le proxy (403), donc AGP, androidx et SDK Android non téléchargeables. Build Android local impossible, validation via CI uniquement. Pas de Xcode (Linux). |
| 2026-11-04 | S1 — vérification des versions | — | — | 0 | `libs.versions.toml` est en retard (voir ci-dessous). Il appartient au socle : la mise à jour doit se faire dans le template puis passer par `sync-socle.sh`. |
| 2026-11-04 | CI | — | — | 0 | Job `ios` (`macos-15`) jamais attribué à un runner, annulé après 15 à 19 min. Quota de minutes macOS probable. |

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
