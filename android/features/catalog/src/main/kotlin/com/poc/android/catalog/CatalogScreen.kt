package com.poc.android.catalog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poc.catalog.CatalogIntent
import com.poc.catalog.CatalogStore
import com.poc.catalog.CatalogUiState
import com.poc.core.format.formatPrice

/** [onAddToCart] est fourni par l'app : l'écran ne connaît pas la feature cart. */
@Composable
fun CatalogScreen(store: CatalogStore, onAddToCart: (productId: String) -> Unit, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    LaunchedEffect(store) { store.dispatch(CatalogIntent.Load) }
    CatalogContent(state = state, onAddToCart = onAddToCart, modifier = modifier)
}

@Composable
fun CatalogContent(state: CatalogUiState, onAddToCart: (String) -> Unit, modifier: Modifier = Modifier) {
    if (state.isLoading) {
        CircularProgressIndicator(modifier = modifier.padding(16.dp))
        return
    }
    LazyColumn(modifier = modifier) {
        items(state.products, key = { it.id }) { product ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("${product.name} — ${formatPrice(product.priceCents)}", modifier = Modifier.weight(1f))
                TextButton(onClick = { onAddToCart(product.id) }) { Text("Ajouter") }
            }
        }
    }
}
