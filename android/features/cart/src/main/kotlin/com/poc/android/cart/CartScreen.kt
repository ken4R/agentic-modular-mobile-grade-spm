package com.poc.android.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.poc.cart.CartIntent
import com.poc.cart.CartStore
import com.poc.cart.CartUiState

@Composable
fun CartScreen(store: CartStore, modifier: Modifier = Modifier) {
    val state by store.state.collectAsState()
    CartContent(state = state, onIntent = store::dispatch, modifier = modifier)
}

@Composable
fun CartContent(state: CartUiState, onIntent: (CartIntent) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (state.lines.isEmpty()) {
            Text("Panier vide")
            return@Column
        }
        state.lines.forEach { line ->
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("${line.quantity} × ${line.name}", modifier = Modifier.weight(1f))
                TextButton(onClick = { onIntent(CartIntent.Remove(line.productId)) }) { Text("Retirer") }
            }
        }
        Text("Total : ${state.totalCents / 100},${(state.totalCents % 100).toString().padStart(2, '0')} €", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { onIntent(CartIntent.Clear) }) { Text("Vider") }
    }
}
