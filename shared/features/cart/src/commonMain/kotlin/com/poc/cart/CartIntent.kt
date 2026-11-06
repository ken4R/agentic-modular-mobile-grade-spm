package com.poc.cart

sealed interface CartIntent {
    data class Add(val productId: String) : CartIntent
    data class Remove(val productId: String) : CartIntent
    data object Clear : CartIntent
}
