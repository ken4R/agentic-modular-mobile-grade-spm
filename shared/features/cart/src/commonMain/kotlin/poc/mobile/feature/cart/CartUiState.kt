package poc.mobile.feature.cart

import poc.mobile.feature.catalog.api.Prices

data class CartUiState(
    val lines: List<CartLine>,
    val error: String?,
) {
    val itemCount: Int get() = lines.sumOf { it.quantity }
    val totalLabel: String get() = Prices.format(lines.sumOf { it.totalCents })

    companion object {
        val Initial = CartUiState(lines = emptyList(), error = null)
    }
}
