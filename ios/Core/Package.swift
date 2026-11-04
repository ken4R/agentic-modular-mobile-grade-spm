// swift-tools-version: 5.9
import PackageDescription

// Shared.xcframework est produit par Gradle (`./gradlew :shared:assembleSharedDebugXCFramework`)
// puis copié ici par scripts/build-ios.sh. SPM exige qu'un binaryTarget local soit dans le package.
let package = Package(
    name: "Core",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "Core", targets: ["Core", "Shared"]),
    ],
    targets: [
        .binaryTarget(name: "Shared", path: "Shared.xcframework"),
        .target(name: "Core", dependencies: ["Shared"]),
    ]
)
