package poc.mobile.shared

import org.koin.core.context.startKoin
import poc.mobile.core.di.coreModule

/** Graphe DI côté iOS, démarré par l'app SwiftUI. Les modules Koin des features s'ajoutent ici. */
object IosGraph {
    fun start() {
        startKoin {
            modules(coreModule)
        }
    }
}
