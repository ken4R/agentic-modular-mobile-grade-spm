package poc.mobile.feature.cart

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import poc.mobile.core.Outcome
import poc.mobile.core.mvi.BaseStore
import kotlin.coroutines.CoroutineContext

class CartStore(
    private val repository: CartRepository,
    context: CoroutineContext,
) : BaseStore(context) {
    private val mutableState = MutableStateFlow(CartUiState.Initial.copy(lines = repository.lines.value))
    val state: StateFlow<CartUiState> = mutableState.asStateFlow()

    init {
        // Le panier est partagé (ajouts depuis le catalogue) : l'état suit le repository.
        scope.launch {
            repository.lines.collect { lines -> mutableState.update { it.copy(lines = lines) } }
        }
    }

    fun accept(intent: CartIntent) {
        mutableState.update { it.copy(error = null) }
        scope.launch {
            when (intent) {
                is CartIntent.Add -> add(intent.productId)
                is CartIntent.Decrement -> repository.decrement(intent.productId)
                is CartIntent.Remove -> repository.remove(intent.productId)
                CartIntent.Clear -> repository.clear()
            }
        }
    }

    private suspend fun add(productId: String) {
        val outcome = repository.add(productId)
        if (outcome is Outcome.Failure) mutableState.update { it.copy(error = outcome.message) }
    }
}
