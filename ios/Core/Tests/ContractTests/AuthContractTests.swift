import Core
import XCTest

/// Contrat SKIE de la feature auth : `state` est consommable comme AsyncSequence.
/// Le `switch` exhaustif sur `AuthIntent` est couvert par `IntentContractTests`.
final class AuthContractTests: XCTestCase {
    func testStateIsObservableAsAsyncSequence() async {
        IosGraph.shared.start()
        let store = IosGraph.shared.authStore()
        defer { store.close() }
        var iterator = store.state.makeAsyncIterator()
        let first = await iterator.next()
        XCTAssertEqual(first?.isAuthenticated, false)
    }
}
