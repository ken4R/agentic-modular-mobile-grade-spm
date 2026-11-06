package com.poc.cart

import com.poc.core.mvi.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CartStore(
    private val addToCart: AddToCartUseCase,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : Store<CartIntent, CartUiState>(CartUiState(), scope) {

    override fun dispatch(intent: CartIntent) {
        when (intent) {
            is CartIntent.Add -> scope.launch {
                val lines = addToCart(state.value.lines, intent.productId)
                reduce { copy(lines = lines) }
            }
            is CartIntent.Remove -> reduce { copy(lines = lines.filterNot { it.productId == intent.productId }) }
            CartIntent.Clear -> reduce { CartUiState() }
        }
    }
}
