import Core
import XCTest

/// Contrat SKIE de la feature catalog : `state` est consommable comme AsyncSequence et
/// `List<ProductSummary>` arrive en `[ProductSummary]` côté Swift.
final class CatalogContractTests: XCTestCase {
    func testStateIsObservableAsAsyncSequence() async {
        IosGraph.shared.start()
        let store = IosGraph.shared.catalogStore()
        defer { store.close() }
        var iterator = store.state.makeAsyncIterator()
        let first = await iterator.next()
        let products: [ProductSummary]? = first?.products
        XCTAssertEqual(products?.count, 0)
    }
}
