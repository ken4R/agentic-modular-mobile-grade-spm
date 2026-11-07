import Core
import Observation

/// Pont SwiftUI ↔ `CatalogStore` (KMP) : reflète `state` et transmet les intents, sans logique propre.
@MainActor
@Observable
public final class CatalogViewModel {
    public private(set) var state: CatalogUiState
    private let store: CatalogStore

    public init(store: CatalogStore) {
        self.store = store
        self.state = store.state.value
    }

    /// Observe le Store jusqu'à l'annulation de la tâche appelante (`.task` de la vue).
    public func observe() async {
        for await next in store.state {
            state = next
        }
    }

    public func send(_ intent: CatalogIntent) {
        store.accept(intent: intent)
    }

    deinit {
        store.close()
    }
}
