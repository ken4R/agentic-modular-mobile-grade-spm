package poc.mobile.core.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.core.persistence.IosSqlDriverFactory
import poc.mobile.core.persistence.SqlDriverFactory

actual val platformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<SqlDriverFactory> { IosSqlDriverFactory() }
}
