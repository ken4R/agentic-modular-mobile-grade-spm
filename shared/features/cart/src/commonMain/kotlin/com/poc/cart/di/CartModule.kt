package com.poc.cart.di

import com.poc.cart.AddToCartUseCase
import com.poc.cart.CartStore
import com.poc.cart.DefaultCartApi
import com.poc.cart.api.CartApi
import org.koin.dsl.module

val cartModule = module {
    factory { AddToCartUseCase(get()) }
    single { CartStore(get()) }
    single<CartApi> { DefaultCartApi(get()) }
}
