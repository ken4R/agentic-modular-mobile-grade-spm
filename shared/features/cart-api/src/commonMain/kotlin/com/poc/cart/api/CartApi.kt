package com.poc.cart.api

import kotlinx.coroutines.flow.Flow

interface CartApi {
    val itemCount: Flow<Int>
}

object CartRoute {
    const val PATH = "cart"
}
