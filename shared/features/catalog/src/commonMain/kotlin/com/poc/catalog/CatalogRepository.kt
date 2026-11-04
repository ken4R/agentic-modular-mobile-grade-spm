package com.poc.catalog

import com.poc.catalog.api.ProductSummary

interface CatalogRepository {
    suspend fun products(): List<ProductSummary>
}

/** Catalogue statique en attendant un backend. */
class FakeCatalogRepository : CatalogRepository {
    override suspend fun products(): List<ProductSummary> = listOf(
        ProductSummary(id = "p1", name = "Café", priceCents = 450),
        ProductSummary(id = "p2", name = "Croissant", priceCents = 180),
        ProductSummary(id = "p3", name = "Jus d'orange", priceCents = 320),
    )
}
