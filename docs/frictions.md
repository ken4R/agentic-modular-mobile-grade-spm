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
