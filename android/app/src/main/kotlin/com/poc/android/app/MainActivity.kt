package com.poc.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.poc.android.auth.AuthScreen
import com.poc.android.cart.CartScreen
import com.poc.android.catalog.CatalogScreen
import com.poc.auth.AuthStore
import com.poc.auth.api.AuthRoute
import com.poc.cart.CartIntent
import com.poc.cart.CartStore
import com.poc.cart.api.CartRoute
import com.poc.catalog.CatalogStore
import com.poc.catalog.api.CatalogRoute
import org.koin.core.context.GlobalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { PocApp() } }
    }
}

/** Composition root : navigation par onglets entre les trois features, aucune logique métier. */
@Composable
private fun PocApp() {
    val koin = remember { GlobalContext.get() }
    val authStore = remember { koin.get<AuthStore>() }
    val catalogStore = remember { koin.get<CatalogStore>() }
    val cartStore = remember { koin.get<CartStore>() }
    var route by rememberSaveable { mutableStateOf(CatalogRoute.PATH) }
    val tabs = listOf(CatalogRoute.PATH to "Catalogue", CartRoute.PATH to "Panier", AuthRoute.PATH to "Compte")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { (path, label) ->
                    NavigationBarItem(
                        selected = route == path,
                        onClick = { route = path },
                        icon = {},
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (route) {
                CatalogRoute.PATH -> CatalogScreen(
                    store = catalogStore,
                    onAddToCart = { cartStore.dispatch(CartIntent.Add(it)) },
                )
                CartRoute.PATH -> CartScreen(cartStore)
                AuthRoute.PATH -> AuthScreen(authStore)
            }
        }
    }
}
