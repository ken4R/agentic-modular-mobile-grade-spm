package com.poc.catalog.di

import com.poc.catalog.CatalogRepository
import com.poc.catalog.CatalogStore
import com.poc.catalog.DefaultCatalogApi
import com.poc.catalog.FakeCatalogRepository
import com.poc.catalog.GetProductsUseCase
import com.poc.catalog.api.CatalogApi
import org.koin.dsl.module

val catalogModule = module {
    single<CatalogRepository> { FakeCatalogRepository() }
    factory { GetProductsUseCase(get()) }
    single { CatalogStore(get()) }
    single<CatalogApi> { DefaultCatalogApi(get()) }
}
