import Core
import Observation

/// Wrapper @Observable autour d'AuthStore (Kotlin), dont l'état est exposé en AsyncSequence par SKIE.
@MainActor
@Observable
public final class AuthViewModel {
    public private(set) var state: AuthUiState
    private let store: AuthStore

    public init(store: AuthStore) {
        self.store = store
        self.state = store.state.value
    }

    public func observe() async {
        for await state in store.state {
            self.state = state
        }
    }

    public func send(_ intent: AuthIntent) {
        store.dispatch(intent: intent)
    }
}
