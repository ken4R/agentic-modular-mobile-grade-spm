package com.poc.cart

import com.poc.catalog.api.CatalogApi

/** Ajoute un produit au panier en le résolvant via le contrat public de catalog. */
class AddToCartUseCase(private val catalog: CatalogApi) {
    suspend operator fun invoke(lines: List<CartLine>, productId: String): List<CartLine> {
        if (lines.any { it.productId == productId }) {
            return lines.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
        }
        val product = catalog.findProduct(productId)
        return if (product == null) {
            lines
        } else {
            lines + CartLine(product.id, product.name, product.priceCents, quantity = 1)
        }
    }
}
