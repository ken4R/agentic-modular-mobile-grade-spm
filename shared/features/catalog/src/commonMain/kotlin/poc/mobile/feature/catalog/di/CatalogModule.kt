package poc.mobile.feature.catalog.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.feature.catalog.CatalogRepository
import poc.mobile.feature.catalog.CatalogStore
import poc.mobile.feature.catalog.InMemoryCatalogRepository
import poc.mobile.feature.catalog.api.CatalogApi

val catalogModule: Module = module {
    single<CatalogRepository> { InMemoryCatalogRepository() }
    single<CatalogApi> { get<CatalogRepository>() }
    factory { CatalogStore(get(), Dispatchers.Main) }
}
