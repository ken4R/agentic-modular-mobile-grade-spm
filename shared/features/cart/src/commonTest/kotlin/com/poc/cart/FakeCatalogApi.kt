package com.poc.cart

import com.poc.catalog.api.CatalogApi
import com.poc.catalog.api.ProductSummary

internal class FakeCatalogApi : CatalogApi {
    private val products = listOf(ProductSummary("p1", "Café", 450), ProductSummary("p2", "Croissant", 180))
    override suspend fun findProduct(id: String): ProductSummary? = products.firstOrNull { it.id == id }
}
