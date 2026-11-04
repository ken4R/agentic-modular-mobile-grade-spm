package com.poc.cart

data class CartLine(val productId: String, val name: String, val unitPriceCents: Long, val quantity: Int) {
    val totalCents: Long get() = unitPriceCents * quantity
}
