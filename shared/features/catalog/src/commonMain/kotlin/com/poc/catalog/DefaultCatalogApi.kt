package com.poc.catalog

import com.poc.catalog.api.CatalogApi
import com.poc.catalog.api.ProductSummary

internal class DefaultCatalogApi(private val repository: CatalogRepository) : CatalogApi {
    override suspend fun findProduct(id: String): ProductSummary? = repository.products().firstOrNull { it.id == id }
}
