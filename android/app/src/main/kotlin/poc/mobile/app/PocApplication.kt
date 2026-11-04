package poc.mobile.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import poc.mobile.core.di.coreModule

class PocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PocApplication)
            modules(coreModule)
        }
    }
}
