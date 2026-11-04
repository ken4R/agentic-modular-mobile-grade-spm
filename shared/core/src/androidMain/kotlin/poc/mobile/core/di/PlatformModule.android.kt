package poc.mobile.core.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.core.persistence.AndroidSqlDriverFactory
import poc.mobile.core.persistence.SqlDriverFactory

actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<SqlDriverFactory> { AndroidSqlDriverFactory(get()) }
}
