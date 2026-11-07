@testable import CartUI
import Core
import XCTest

/// Vérifie le câblage vue ↔ Store : les intents envoyés par la vue mettent à jour l'état observé.
@MainActor
final class CartViewTests: XCTestCase {
    private var viewModel: CartViewModel?

    override func setUp() async throws {
        IosGraph.shared.start()
        let viewModel = CartViewModel(store: IosGraph.shared.cartStore())
        // Le panier est un singleton du graphe : on repart d'un panier vide.
        viewModel.send(CartIntentClear.shared)
        self.viewModel = viewModel
    }

    func testAddThenDecrementUpdatesLines() async throws {
        let viewModel = try XCTUnwrap(viewModel)
        let observation = Task { await viewModel.observe() }
        defer { observation.cancel() }

        viewModel.send(CartIntentAdd(productId: "p1"))
        viewModel.send(CartIntentAdd(productId: "p1"))
        try await waitUntil { viewModel.state.itemCount == 2 }
        XCTAssertEqual(viewModel.state.totalLabel, "9,00 €")

        viewModel.send(CartIntentDecrement(productId: "p1"))
        try await waitUntil { viewModel.state.itemCount == 1 }
    }

    func testUnknownProductShowsError() async throws {
        let viewModel = try XCTUnwrap(viewModel)
        let observation = Task { await viewModel.observe() }
        defer { observation.cancel() }

        viewModel.send(CartIntentAdd(productId: "inconnu"))

        try await waitUntil { viewModel.state.error != nil }
        XCTAssertTrue(viewModel.state.lines.isEmpty)
    }

    private func waitUntil(timeout: Duration = .seconds(2), _ condition: () -> Bool) async throws {
        let deadline = ContinuousClock.now + timeout
        while !condition() {
            guard ContinuousClock.now < deadline else { return XCTFail("condition non atteinte") }
            try await Task.sleep(for: .milliseconds(20))
        }
    }
}
