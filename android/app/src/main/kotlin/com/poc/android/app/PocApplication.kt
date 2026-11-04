package com.poc.android.app

import android.app.Application
import com.poc.auth.di.authModule
import com.poc.cart.di.cartModule
import com.poc.catalog.di.catalogModule
import com.poc.core.di.coreModule
import com.poc.core.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PocApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PocApplication)
            modules(coreModule, platformModule, authModule, catalogModule, cartModule)
        }
    }
}
