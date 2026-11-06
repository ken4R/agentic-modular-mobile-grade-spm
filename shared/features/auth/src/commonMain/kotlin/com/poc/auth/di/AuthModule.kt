package com.poc.auth.di

import com.poc.auth.AuthRepository
import com.poc.auth.AuthStore
import com.poc.auth.DefaultAuthApi
import com.poc.auth.FakeAuthRepository
import com.poc.auth.LoginUseCase
import com.poc.auth.api.AuthApi
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { FakeAuthRepository() }
    factory { LoginUseCase(get()) }
    single { AuthStore(get()) }
    single<AuthApi> { DefaultAuthApi(get()) }
}
