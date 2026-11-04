package com.poc.catalog.api

/** Résumé d'un produit exposé aux autres features (ex. cart). */
data class ProductSummary(val id: String, val name: String, val priceCents: Long)

interface CatalogApi {
    suspend fun findProduct(id: String): ProductSummary?
}

object CatalogRoute {
    const val PATH = "catalog"
}
