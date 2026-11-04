package com.poc.core.di

import com.poc.core.db.AppDatabase
import com.poc.core.logging.Logger
import com.poc.core.logging.PrintLogger
import com.poc.core.network.createHttpClient
import org.koin.core.module.Module
import org.koin.dsl.module

/** Dépendances propres à la plateforme (driver SQLDelight). */
expect val platformModule: Module

val coreModule: Module = module {
    single<Logger> { PrintLogger }
    single { createHttpClient() }
    single { AppDatabase(get()) }
}
