package poc.mobile.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import poc.mobile.core.di.coreModule
import poc.mobile.feature.auth.di.authModule
import poc.mobile.feature.cart.di.cartModule
import poc.mobile.feature.catalog.di.catalogModule

class PocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PocApplication)
            modules(coreModule, authModule, catalogModule, cartModule)
        }
    }
}
