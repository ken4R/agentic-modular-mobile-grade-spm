import Core
import XCTest

/// Contrat SKIE de la feature cart : `state` est consommable comme AsyncSequence et
/// les propriétés calculées (`itemCount`, `totalLabel`) sont visibles côté Swift.
final class CartContractTests: XCTestCase {
    func testStateIsObservableAsAsyncSequence() async {
        IosGraph.shared.start()
        let store = IosGraph.shared.cartStore()
        defer { store.close() }
        var iterator = store.state.makeAsyncIterator()
        let first = await iterator.next()
        XCTAssertNotNil(first?.totalLabel)
        XCTAssertGreaterThanOrEqual(first?.itemCount ?? -1, 0)
    }
}
