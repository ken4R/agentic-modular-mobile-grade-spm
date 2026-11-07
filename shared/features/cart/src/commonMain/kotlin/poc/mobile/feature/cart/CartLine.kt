package poc.mobile.feature.cart

import poc.mobile.feature.catalog.api.Prices

data class CartLine(val productId: String, val name: String, val unitPriceCents: Long, val quantity: Int) {
    val totalCents: Long get() = unitPriceCents * quantity
    val totalLabel: String get() = Prices.format(totalCents)
}
