package com.poc.core.db

import app.cash.sqldelight.db.SqlDriver

const val DATABASE_NAME = "poc.db"

/** Crée le driver SQLDelight de la plateforme. */
expect class DriverFactory {
    fun create(): SqlDriver
}
