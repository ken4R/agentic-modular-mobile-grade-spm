package poc.mobile.feature.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import poc.mobile.core.Outcome
import poc.mobile.feature.cart.api.CartApi
import poc.mobile.feature.catalog.api.CatalogApi

interface CartRepository : CartApi {
    val lines: StateFlow<List<CartLine>>
    suspend fun add(productId: String): Outcome<Unit>
    suspend fun decrement(productId: String)
    suspend fun remove(productId: String)
    suspend fun clear()
}

/** Panier en mémoire ; les produits sont résolus via le contrat public de catalog. */
internal class InMemoryCartRepository(private val catalog: CatalogApi) : CartRepository {
    private val mutableLines = MutableStateFlow<List<CartLine>>(emptyList())
    override val lines: StateFlow<List<CartLine>> = mutableLines.asStateFlow()
    override val itemCount: Flow<Int> = lines.map { lines -> lines.sumOf { it.quantity } }

    override suspend fun add(productId: String): Outcome<Unit> {
        if (mutableLines.value.any { it.productId == productId }) {
            mutableLines.update { lines ->
                lines.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
            }
            return Outcome.Success(Unit)
        }
        val product = catalog.findProduct(productId) ?: return Outcome.Failure("Produit introuvable")
        mutableLines.update { it + CartLine(product.id, product.name, product.priceCents, quantity = 1) }
        return Outcome.Success(Unit)
    }

    override suspend fun decrement(productId: String) {
        mutableLines.update { lines ->
            lines.mapNotNull { line ->
                when {
                    line.productId != productId -> line
                    line.quantity > 1 -> line.copy(quantity = line.quantity - 1)
                    else -> null
                }
            }
        }
    }

    override suspend fun remove(productId: String) {
        mutableLines.update { lines -> lines.filterNot { it.productId == productId } }
    }

    override suspend fun clear() {
        mutableLines.value = emptyList()
    }
}
