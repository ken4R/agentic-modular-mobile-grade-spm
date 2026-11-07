package poc.mobile.feature.cart.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.feature.cart.CartRepository
import poc.mobile.feature.cart.CartStore
import poc.mobile.feature.cart.InMemoryCartRepository
import poc.mobile.feature.cart.api.CartApi

/** Requiert un `CatalogApi` dans le graphe (fourni par catalogModule). */
val cartModule: Module = module {
    single<CartRepository> { InMemoryCartRepository(get()) }
    single<CartApi> { get<CartRepository>() }
    factory { CartStore(get(), Dispatchers.Main) }
}
