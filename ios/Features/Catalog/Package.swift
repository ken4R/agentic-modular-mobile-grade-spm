// swift-tools-version: 5.10
import PackageDescription

let package = Package(
    name: "Catalog",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "CatalogUI", targets: ["CatalogUI"])
    ],
    dependencies: [
        .package(path: "../../Core")
    ],
    targets: [
        .target(name: "CatalogUI", dependencies: [.product(name: "Core", package: "Core")]),
        .testTarget(name: "CatalogUITests", dependencies: ["CatalogUI"])
    ]
)
