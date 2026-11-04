package com.poc.core.di

import app.cash.sqldelight.db.SqlDriver
import com.poc.core.db.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> { DriverFactory().create() }
}
