package com.poc.cart

import com.poc.cart.api.CartApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class DefaultCartApi(store: CartStore) : CartApi {
    override val itemCount: Flow<Int> = store.state.map { it.itemCount }.distinctUntilChanged()
}
