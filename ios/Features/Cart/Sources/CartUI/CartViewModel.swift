import Core
import Observation

/// Pont SwiftUI ↔ `CartStore` (KMP) : reflète `state` et transmet les intents, sans logique propre.
@MainActor
@Observable
public final class CartViewModel {
    public private(set) var state: CartUiState
    private let store: CartStore

    public init(store: CartStore) {
        self.store = store
        self.state = store.state.value
    }

    /// Observe le Store jusqu'à l'annulation de la tâche appelante (`.task` de la vue).
    public func observe() async {
        for await next in store.state {
            state = next
        }
    }

    public func send(_ intent: CartIntent) {
        store.accept(intent: intent)
    }

    deinit {
        store.close()
    }
}
