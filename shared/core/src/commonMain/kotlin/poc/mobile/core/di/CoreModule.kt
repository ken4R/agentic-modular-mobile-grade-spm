package poc.mobile.core.di

import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.core.logging.Logger
import poc.mobile.core.logging.PrintLogger
import poc.mobile.core.network.createHttpClient

/** Moteur HTTP et fabrique de drivers SQL, propres à chaque plateforme. */
expect val platformModule: Module

val coreModule: Module = module {
    includes(platformModule)
    single<Logger> { PrintLogger() }
    single { createHttpClient(get()) }
}
