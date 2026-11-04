package poc.mobile.core.persistence

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema

/** Chaque feature persistante fournit son schéma SQLDelight ; core ne connaît aucune table. */
expect class SqlDriverFactory {
    fun create(schema: SqlSchema<QueryResult.Value<Unit>>, name: String): SqlDriver
}
