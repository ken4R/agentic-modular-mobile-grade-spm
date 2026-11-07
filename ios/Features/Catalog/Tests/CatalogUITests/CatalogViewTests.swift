@testable import CatalogUI
import Core
import XCTest

/// Vérifie le câblage vue ↔ Store : l'intent `Load` publie les produits dans l'état observé.
@MainActor
final class CatalogViewTests: XCTestCase {
    private var viewModel: CatalogViewModel?

    override func setUp() async throws {
        IosGraph.shared.start()
        viewModel = CatalogViewModel(store: IosGraph.shared.catalogStore())
    }

    func testLoadPublishesProducts() async throws {
        let viewModel = try XCTUnwrap(viewModel)
        let observation = Task { await viewModel.observe() }
        defer { observation.cancel() }

        viewModel.send(CatalogIntentLoad.shared)

        try await waitUntil { !viewModel.state.products.isEmpty }
        XCTAssertEqual(viewModel.state.products.first?.priceLabel, "4,50 €")
        XCTAssertNil(viewModel.state.error)
    }

    private func waitUntil(timeout: Duration = .seconds(2), _ condition: () -> Bool) async throws {
        let deadline = ContinuousClock.now + timeout
        while !condition() {
            guard ContinuousClock.now < deadline else { return XCTFail("condition non atteinte") }
            try await Task.sleep(for: .milliseconds(20))
        }
    }
}
