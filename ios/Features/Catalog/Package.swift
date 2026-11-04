// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CatalogUI",
    platforms: [.iOS(.v17)],
    products: [
        .library(name: "CatalogUI", targets: ["CatalogUI"]),
    ],
    dependencies: [
        .package(path: "../../Core"),
    ],
    targets: [
        .target(name: "CatalogUI", dependencies: [.product(name: "Core", package: "Core")]),
    ]
)
