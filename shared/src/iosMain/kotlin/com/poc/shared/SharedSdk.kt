package com.poc.shared

import com.poc.auth.AuthStore
import com.poc.auth.di.authModule
import com.poc.cart.CartStore
import com.poc.cart.di.cartModule
import com.poc.catalog.CatalogStore
import com.poc.catalog.di.catalogModule
import com.poc.core.di.coreModule
import com.poc.core.di.platformModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

/** Point d'entrée iOS : démarre Koin et fournit les Stores à la couche SwiftUI. */
object SharedSdk : KoinComponent {
    fun start() {
        startKoin {
            modules(coreModule, platformModule, authModule, catalogModule, cartModule)
        }
    }

    fun authStore(): AuthStore = get()
    fun catalogStore(): CatalogStore = get()
    fun cartStore(): CartStore = get()
}
