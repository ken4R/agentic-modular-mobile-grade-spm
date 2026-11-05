import Core
import XCTest

/// Contrat SKIE de la feature auth : `state` est consommable comme AsyncSequence.
/// Le `switch` exhaustif sur `AuthIntent` (exigé par expose-to-swift) est en attente :
/// `onEnum(of:)` n'est pas généré pour cette sealed interface (docs/frictions.md, étape 8).
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
