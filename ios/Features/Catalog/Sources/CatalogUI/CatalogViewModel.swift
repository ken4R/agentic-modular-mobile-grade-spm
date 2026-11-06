import Core
import Observation

@MainActor
@Observable
public final class CatalogViewModel {
    public private(set) var state: CatalogUiState
    private let store: CatalogStore

    public init(store: CatalogStore) {
        self.store = store
        self.state = store.state.value
    }

    public func observe() async {
        for await state in store.state {
            self.state = state
        }
    }

    public func load() {
        store.dispatch(intent: CatalogIntentLoad.shared)
    }
}
