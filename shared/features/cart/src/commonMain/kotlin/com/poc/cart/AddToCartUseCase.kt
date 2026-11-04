package com.poc.cart

import com.poc.catalog.api.CatalogApi

/** Ajoute un produit au panier en le résolvant via le contrat public de catalog. */
class AddToCartUseCase(private val catalog: CatalogApi) {
    suspend operator fun invoke(lines: List<CartLine>, productId: String): List<CartLine> {
        val existing = lines.firstOrNull { it.productId == productId }
        if (existing != null) {
            return lines.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
        }
        val product = catalog.findProduct(productId) ?: return lines
        return lines + CartLine(product.id, product.name, product.priceCents, quantity = 1)
    }
}
