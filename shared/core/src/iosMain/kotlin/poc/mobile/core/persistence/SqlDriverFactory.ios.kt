package poc.mobile.core.persistence

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class SqlDriverFactory {
    actual fun create(schema: SqlSchema<QueryResult.Value<Unit>>, name: String): SqlDriver =
        NativeSqliteDriver(schema, name)
}
