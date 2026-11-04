import Core
import Observation

@MainActor
@Observable
public final class CartViewModel {
    public private(set) var state: CartUiState
    private let store: CartStore

    public init(store: CartStore) {
        self.store = store
        self.state = store.state.value
    }

    public func observe() async {
        for await state in store.state {
            self.state = state
        }
    }

    public func send(_ intent: CartIntent) {
        store.dispatch(intent: intent)
    }
}
