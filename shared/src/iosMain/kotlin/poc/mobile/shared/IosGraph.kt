package poc.mobile.shared

import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform
import poc.mobile.core.di.coreModule
import poc.mobile.feature.auth.AuthStore
import poc.mobile.feature.auth.di.authModule

/**
 * Graphe DI côté iOS, démarré par l'app SwiftUI. Les modules Koin des features s'ajoutent ici,
 * avec un accesseur par Store (Swift ne peut pas appeler les `inline reified` de Koin).
 */
object IosGraph {
    /** Idempotent : l'app et les tests peuvent l'appeler chacun. */
    fun start() {
        if (KoinPlatform.getKoinOrNull() != null) return
        startKoin {
            modules(coreModule, authModule)
        }
    }

    fun authStore(): AuthStore = KoinPlatform.getKoin().get()
}
