<<<<<<< HEAD
// swift-tools-version: 5.9
import PackageDescription

// Shared.xcframework est produit par Gradle (`./gradlew :shared:assembleSharedDebugXCFramework`)
// puis copié ici par scripts/build-ios.sh. SPM exige qu'un binaryTarget local soit dans le package.
=======
// swift-tools-version: 5.10
import PackageDescription

// Shared.xcframework est produit par Gradle (./gradlew :shared:assembleSharedDebugXCFramework).
>>>>>>> origin/main
let package = Package(
    name: "Core",
    platforms: [.iOS(.v17)],
    products: [
<<<<<<< HEAD
        .library(name: "Core", targets: ["Core", "Shared"]),
    ],
    targets: [
        .binaryTarget(name: "Shared", path: "Shared.xcframework"),
        // Shared est un framework statique : le driver SQLDelight natif (SQLiter) a besoin de libsqlite3 au link de l'app.
        .target(name: "Core", dependencies: ["Shared"], linkerSettings: [.linkedLibrary("sqlite3")]),
=======
        .library(name: "Core", targets: ["Core", "Shared"])
    ],
    targets: [
        .binaryTarget(name: "Shared", path: "../../shared/build/XCFrameworks/debug/Shared.xcframework"),
        .target(
            name: "Core",
            dependencies: ["Shared"],
            // Framework Kotlin statique : le driver natif SQLDelight a besoin de libsqlite3.
            linkerSettings: [.linkedLibrary("sqlite3")]
        ),
        .testTarget(name: "ContractTests", dependencies: ["Core"])
>>>>>>> origin/main
    ]
)
