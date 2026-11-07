package poc.mobile.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import poc.mobile.core.mvi.BaseStore
import poc.mobile.feature.auth.AuthStore
import poc.mobile.feature.auth.api.AuthRoute
import poc.mobile.feature.auth.ui.AuthScreen
import poc.mobile.feature.cart.CartIntent
import poc.mobile.feature.cart.CartStore
import poc.mobile.feature.cart.api.CartRoute
import poc.mobile.feature.cart.ui.CartScreen
import poc.mobile.feature.catalog.CatalogStore
import poc.mobile.feature.catalog.api.CatalogRoute
import poc.mobile.feature.catalog.ui.CatalogScreen

/** Onglets de l'app : une route par feature (skill new-feature-module). */
private val tabs = listOf(
    AuthRoute.PATH to "Compte",
    CatalogRoute.PATH to "Catalogue",
    CartRoute.PATH to "Panier",
)

/** Navigation de l'app : une route par feature, Stores fournis par Koin (skill new-feature-module). */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    // Panier partagé entre les onglets : le catalogue y ajoute, l'onglet panier l'affiche.
    val cartStore = rememberStore<CartStore>()
    Scaffold(bottomBar = { AppTabBar(navController) }) { padding ->
        NavHost(
            navController = navController,
            startDestination = AuthRoute.PATH,
            modifier = Modifier.padding(padding),
        ) {
            composable(AuthRoute.PATH) {
                AuthScreen(store = rememberStore<AuthStore>())
            }
            composable(CatalogRoute.PATH) {
                CatalogScreen(
                    store = rememberStore<CatalogStore>(),
                    onAddToCart = { cartStore.accept(CartIntent.Add(it)) },
                )
            }
            composable(CartRoute.PATH) {
                CartScreen(store = cartStore)
            }
        }
    }
}

@Composable
private fun AppTabBar(navController: NavHostController) {
    val entry by navController.currentBackStackEntryAsState()
    NavigationBar {
        tabs.forEach { (route, label) ->
            NavigationBarItem(
                selected = entry?.destination?.route == route,
                onClick = {
                    // Onglets : une seule entrée par onglet dans la pile, retour vers l'onglet de départ.
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {},
                label = { Text(label) },
            )
        }
    }
}

/** Store fourni par Koin, fermé quand la destination quitte la composition. */
@Composable
private inline fun <reified T : BaseStore> rememberStore(): T {
    val store = koinInject<T>()
    DisposableEffect(store) { onDispose { store.close() } }
    return store
}
