package poc.mobile.feature.catalog

import poc.mobile.core.Outcome
import poc.mobile.feature.catalog.api.CatalogApi
import poc.mobile.feature.catalog.api.ProductSummary

interface CatalogRepository : CatalogApi {
    suspend fun products(): Outcome<List<ProductSummary>>
}

/** Pas de backend dans le POC : catalogue statique. */
internal class InMemoryCatalogRepository : CatalogRepository {
    private val products = listOf(
        ProductSummary(id = "p1", name = "Café", priceCents = 450),
        ProductSummary(id = "p2", name = "Croissant", priceCents = 180),
        ProductSummary(id = "p3", name = "Jus d'orange", priceCents = 320),
    )

    override suspend fun products(): Outcome<List<ProductSummary>> = Outcome.Success(products)

    override suspend fun findProduct(id: String): ProductSummary? = products.firstOrNull { it.id == id }
}
