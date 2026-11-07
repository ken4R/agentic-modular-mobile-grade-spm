package poc.mobile.feature.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import poc.mobile.feature.cart.CartIntent
import poc.mobile.feature.cart.CartLine
import poc.mobile.feature.cart.CartStore

/** Panier : affiche l'état du Store et lui transmet les intents, sans logique propre. */
@Composable
fun CartScreen(store: CartStore, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        state.error?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.testTag(CartTags.ERROR))
        }
        if (state.lines.isEmpty()) {
            Text("Panier vide", modifier = Modifier.testTag(CartTags.EMPTY))
            return@Column
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.lines, key = { it.productId }) { line -> CartLineRow(line, store::accept) }
        }
        Text("${state.itemCount} article(s) — ${state.totalLabel}", modifier = Modifier.testTag(CartTags.TOTAL))
        Button(onClick = { store.accept(CartIntent.Clear) }, modifier = Modifier.testTag(CartTags.CLEAR)) {
            Text("Vider le panier")
        }
    }
}

@Composable
private fun CartLineRow(line: CartLine, send: (CartIntent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).testTag(CartTags.line(line.productId)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("${line.name} × ${line.quantity} — ${line.totalLabel}", modifier = Modifier.weight(1f))
        TextButton(onClick = { send(CartIntent.Decrement(line.productId)) }) { Text("−") }
        TextButton(onClick = { send(CartIntent.Add(line.productId)) }) { Text("+") }
        TextButton(onClick = { send(CartIntent.Remove(line.productId)) }) { Text("Retirer") }
    }
}

object CartTags {
    const val EMPTY = "cart.empty"
    const val ERROR = "cart.error"
    const val TOTAL = "cart.total"
    const val CLEAR = "cart.clear"
    fun line(id: String) = "cart.line.$id"
}
