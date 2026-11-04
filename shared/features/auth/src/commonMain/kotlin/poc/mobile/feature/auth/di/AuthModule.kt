package poc.mobile.feature.auth.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import poc.mobile.feature.auth.AuthRepository
import poc.mobile.feature.auth.AuthStore
import poc.mobile.feature.auth.InMemoryAuthRepository
import poc.mobile.feature.auth.api.AuthApi

val authModule: Module = module {
    single<AuthRepository> { InMemoryAuthRepository() }
    single<AuthApi> { get<AuthRepository>() }
    factory { AuthStore(get(), Dispatchers.Main) }
}
