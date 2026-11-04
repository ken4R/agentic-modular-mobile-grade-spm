// swift-tools-version: 5.10
import PackageDescription

// Shared.xcframework est produit par Gradle (./gradlew :shared:assembleSharedDebugXCFramework).
let package = Package(
    name: "Core",
    platforms: [.iOS(.v17)],
    products: [
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
    ]
)
