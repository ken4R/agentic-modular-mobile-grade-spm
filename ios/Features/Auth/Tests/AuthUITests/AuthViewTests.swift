@testable import AuthUI
import Core
import XCTest

/// Vérifie le câblage vue ↔ Store : les intents envoyés par la vue mettent à jour l'état observé.
@MainActor
final class AuthViewTests: XCTestCase {
    private var viewModel: AuthViewModel?

    override func setUp() async throws {
        IosGraph.shared.start()
        viewModel = AuthViewModel(store: IosGraph.shared.authStore())
    }

    func testValidLoginAuthenticates() async throws {
        let viewModel = try XCTUnwrap(viewModel)
        let observation = Task { await viewModel.observe() }
        defer { observation.cancel() }

        viewModel.send(AuthIntentEmailChanged(value: "a@b.c"))
        viewModel.send(AuthIntentPasswordChanged(value: "secret"))
        viewModel.send(AuthIntentSubmit.shared)

        try await waitUntil { viewModel.state.isAuthenticated }
        XCTAssertEqual(viewModel.state.email, "a@b.c")
    }

    func testInvalidEmailShowsError() async throws {
        let viewModel = try XCTUnwrap(viewModel)
        let observation = Task { await viewModel.observe() }
        defer { observation.cancel() }

        viewModel.send(AuthIntentEmailChanged(value: "invalide"))
        viewModel.send(AuthIntentPasswordChanged(value: "secret"))
        viewModel.send(AuthIntentSubmit.shared)

        try await waitUntil { viewModel.state.error != nil }
        XCTAssertFalse(viewModel.state.isAuthenticated)
    }

    private func waitUntil(timeout: Duration = .seconds(2), _ condition: () -> Bool) async throws {
        let deadline = ContinuousClock.now + timeout
        while !condition() {
            guard ContinuousClock.now < deadline else { return XCTFail("condition non atteinte") }
            try await Task.sleep(for: .milliseconds(20))
        }
    }
}
