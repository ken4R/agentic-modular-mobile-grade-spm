import Core
import Observation

/// Pont SwiftUI ↔ `AuthStore` (KMP) : reflète `state` et transmet les intents, sans logique propre.
@MainActor
@Observable
public final class AuthViewModel {
    public private(set) var state: AuthUiState
    private let store: AuthStore

    public init(store: AuthStore) {
        self.store = store
        self.state = store.state.value
    }

    /// Observe le Store jusqu'à l'annulation de la tâche appelante (`.task` de la vue).
    public func observe() async {
        for await next in store.state {
            state = next
        }
    }

    public func send(_ intent: AuthIntent) {
        store.accept(intent: intent)
    }

    deinit {
        store.close()
    }
}
