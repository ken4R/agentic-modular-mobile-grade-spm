package poc.mobile.feature.catalog.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import poc.mobile.feature.catalog.CatalogIntent
import poc.mobile.feature.catalog.CatalogStore

/**
 * Liste des produits : affiche l'état du Store et lui transmet les intents, sans logique propre.
 * [onAddToCart] est fourni par l'app : l'écran ne connaît pas la feature cart.
 */
@Composable
fun CatalogScreen(store: CatalogStore, onAddToCart: (productId: String) -> Unit, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    LaunchedEffect(store) { store.accept(CatalogIntent.Load) }
    when {
        state.isLoading -> CircularProgressIndicator(modifier = modifier.padding(24.dp).testTag(CatalogTags.LOADING))
        state.error != null -> Text(
            state.error.orEmpty(),
            color = MaterialTheme.colorScheme.error,
            modifier = modifier.padding(24.dp).testTag(CatalogTags.ERROR),
        )
        else -> LazyColumn(modifier = modifier.fillMaxSize()) {
            items(state.products, key = { it.id }) { product ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "${product.name} — ${product.priceLabel}",
                        modifier = Modifier.weight(1f).testTag(CatalogTags.product(product.id)),
                    )
                    TextButton(
                        onClick = { onAddToCart(product.id) },
                        modifier = Modifier.testTag(CatalogTags.add(product.id)),
                    ) { Text("Ajouter") }
                }
            }
        }
    }
}

object CatalogTags {
    const val LOADING = "catalog.loading"
    const val ERROR = "catalog.error"
    fun product(id: String) = "catalog.product.$id"
    fun add(id: String) = "catalog.add.$id"
}
