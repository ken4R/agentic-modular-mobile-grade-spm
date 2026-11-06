import Core
import Observation

<<<<<<< HEAD
/// Wrapper @Observable autour d'AuthStore (Kotlin), dont l'état est exposé en AsyncSequence par SKIE.
=======
/// Pont SwiftUI ↔ `AuthStore` (KMP) : reflète `state` et transmet les intents, sans logique propre.
>>>>>>> origin/main
@MainActor
@Observable
public final class AuthViewModel {
    public private(set) var state: AuthUiState
    private let store: AuthStore

    public init(store: AuthStore) {
        self.store = store
        self.state = store.state.value
    }

<<<<<<< HEAD
    public func observe() async {
        for await state in store.state {
            self.state = state
=======
    /// Observe le Store jusqu'à l'annulation de la tâche appelante (`.task` de la vue).
    public func observe() async {
        for await next in store.state {
            state = next
>>>>>>> origin/main
        }
    }

    public func send(_ intent: AuthIntent) {
<<<<<<< HEAD
        store.dispatch(intent: intent)
=======
        store.accept(intent: intent)
    }

    deinit {
        store.close()
>>>>>>> origin/main
    }
}
