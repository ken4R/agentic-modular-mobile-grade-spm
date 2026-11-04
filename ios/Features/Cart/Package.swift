// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CartUI",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "CartUI", targets: ["CartUI"]),
    ],
    dependencies: [
        .package(path: "../../Core"),
    ],
    targets: [
        .target(name: "CartUI", dependencies: [.product(name: "Core", package: "Core")]),
    ]
)
