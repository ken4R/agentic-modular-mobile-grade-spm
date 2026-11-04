package com.poc.core.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun create(): SqlDriver = NativeSqliteDriver(AppDatabase.Schema, DATABASE_NAME)
}
