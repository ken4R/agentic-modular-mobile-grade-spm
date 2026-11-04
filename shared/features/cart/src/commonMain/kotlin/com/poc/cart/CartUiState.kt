package com.poc.cart

data class CartUiState(val lines: List<CartLine> = emptyList()) {
    val itemCount: Int get() = lines.sumOf { it.quantity }
    val totalCents: Long get() = lines.sumOf { it.totalCents }
}
