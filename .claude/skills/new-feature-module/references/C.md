# Approche C — fichiers de build à générer

- `shared/features/<name>/build.gradle.kts` et `<name>-api/build.gradle.kts` : convention plugins `kmp-feature` / `kmp-api`.
- `android/features/<name>/build.gradle.kts` : convention plugin `android-feature`.
- Ajouter les trois modules dans `settings.gradle.kts`.
- Ajouter `<name>` à `exportedModules` du module parapluie `:shared` (dossier `shared/`).
- DI iOS : enregistrer le module Koin de la feature dans le graphe démarré côté Kotlin par le parapluie
  (ex. `IosGraph.start()`), pas dans le code Swift de `ios/App`.
- `ios/Features/<Name>/Package.swift` : écrit à la main, dépend de `Core` et du `binaryTarget` `Shared`.
- Ajouter le package en dépendance locale dans `ios/App/Package.swift` (ou dans le `.xcodeproj` via "Add Local Package").
  Si l'app est générée par XcodeGen (`ios/project.yml`) et que `App.xcodeproj` est commité (CI sans XcodeGen),
  mettre à jour les deux : `project.yml`, puis régénérer, ou à défaut écrire les entrées pbxproj du package
  (`XCLocalSwiftPackageReference`, `XCSwiftPackageProductDependency`, build file, frameworks).
- **Aucun fichier Bazel.**
