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
