package poc.mobile.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import poc.mobile.core.mvi.BaseStore
import poc.mobile.feature.auth.AuthStore
import poc.mobile.feature.auth.api.AuthRoute
import poc.mobile.feature.auth.ui.AuthScreen

/** Navigation de l'app : une route par feature, Stores fournis par Koin (skill new-feature-module). */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthRoute.PATH) {
        composable(AuthRoute.PATH) {
            AuthScreen(store = rememberStore<AuthStore>())
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
