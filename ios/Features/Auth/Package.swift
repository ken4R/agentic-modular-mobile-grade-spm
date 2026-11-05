<<<<<<< HEAD
// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "AuthUI",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "AuthUI", targets: ["AuthUI"]),
    ],
    dependencies: [
        .package(path: "../../Core"),
    ],
    targets: [
        .target(name: "AuthUI", dependencies: [.product(name: "Core", package: "Core")]),
=======
// swift-tools-version: 5.10
import PackageDescription

let package = Package(
    name: "Auth",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "AuthUI", targets: ["AuthUI"])
    ],
    dependencies: [
        .package(path: "../../Core")
    ],
    targets: [
        .target(name: "AuthUI", dependencies: [.product(name: "Core", package: "Core")]),
        .testTarget(name: "AuthUITests", dependencies: ["AuthUI"])
>>>>>>> origin/main
    ]
)
