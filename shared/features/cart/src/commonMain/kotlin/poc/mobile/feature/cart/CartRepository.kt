package poc.mobile.feature.cart

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    // Sérialise les ajouts : sans verrou, deux ajouts du même produit pendant `findProduct`
    // (qui suspend) créeraient deux lignes pour un même `productId`.
    private val addLock = Mutex()

    override suspend fun add(productId: String): Outcome<Unit> = addLock.withLock {
        val known = mutableLines.value.any { it.productId == productId }
        val product = if (known) null else catalog.findProduct(productId)
        if (!known && product == null) return@withLock Outcome.Failure("Produit introuvable")
        mutableLines.update { lines ->
            when {
                lines.any { it.productId == productId } ->
                    lines.map { if (it.productId == productId) it.copy(quantity = it.quantity + 1) else it }
                product != null -> lines + CartLine(product.id, product.name, product.priceCents, quantity = 1)
                // Ligne retirée entre-temps par `remove`/`decrement` : le retrait l'emporte.
                else -> lines
            }
        }
        Outcome.Success(Unit)
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
