package poc.mobile.feature.catalog.api

import poc.mobile.core.Prices

/** Contrat public de la feature catalog, consommable par les autres features (ex. cart). */
interface CatalogApi {
    /** Produit du catalogue, `null` s'il n'existe pas. */
    suspend fun findProduct(id: String): ProductSummary?
}

data class ProductSummary(val id: String, val name: String, val priceCents: Long) {
    val priceLabel: String get() = Prices.format(priceCents)
}

object CatalogRoute {
    const val PATH = "catalog"
}
