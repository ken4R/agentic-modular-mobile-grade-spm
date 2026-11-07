# ADR-0001 — Approche C : Gradle + SPM, sans Bazel (baseline)

- **Date** : 2026-11-04
- **Statut** : accepté
- **Approche** : C

## Contexte
Le plan d'action (`docs/plan-action.md`) compare trois approches de build pour un monorepo mobile
modulaire : logique partagée en KMP, UI Compose et SwiftUI, modules iOS en SPM.
Bazel ne compile pas Kotlin/Native (`rules_kotlin` couvre JVM, Android et JS) : Gradle reste
obligatoire pour la cible iOS du code KMP, quelle que soit l'approche.
Ce repo porte le POC C, premier dans l'ordre conseillé (C → A → B).

## Options considérées
1. **A** — Gradle pour KMP, SPM natif, Bazel orchestrateur iOS.
2. **B** — Bazel « full » Android/Swift, Gradle isolé pour Kotlin/Native.
3. **C** — Gradle + SPM optimisés, sans Bazel.

## Décision
Ce repo implémente l'approche C. Elle sert de baseline chiffrée pour juger A et B, au risque le plus faible
(2 semaines estimées).

## Conséquences
- Gradle pour tout le Kotlin, SPM pour tout le Swift, Xcode natif. Aucun fichier Bazel dans ce repo.
- Optimisations à mesurer en semaine 2 : configuration cache, build cache distant, build par impact
  (`gradle-dependency-graph`), cache DerivedData en CI.
- `Package.swift` écrits à la main, `binaryTarget` vers un unique `Shared.xcframework` produit par Gradle.
- Les frictions de build sont consignées dans `docs/frictions.md`. Bazel n'y est jamais proposé comme remède.
- Fin de semaine 1, ce repo sert de point de départ au fork du POC A.
- Critère de réévaluation : les résultats de la grille de mesure (section 5 du plan) comparés à A et B.
