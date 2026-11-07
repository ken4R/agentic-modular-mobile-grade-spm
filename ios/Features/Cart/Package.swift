// swift-tools-version: 5.10
import PackageDescription

let package = Package(
    name: "Cart",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "CartUI", targets: ["CartUI"])
    ],
    dependencies: [
        .package(path: "../../Core")
    ],
    targets: [
        .target(name: "CartUI", dependencies: [.product(name: "Core", package: "Core")]),
        .testTarget(name: "CartUITests", dependencies: ["CartUI"])
    ]
)
