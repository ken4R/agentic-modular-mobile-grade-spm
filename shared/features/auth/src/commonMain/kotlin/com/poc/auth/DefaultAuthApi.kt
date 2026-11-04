package com.poc.auth

import com.poc.auth.api.AuthApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class DefaultAuthApi(store: AuthStore) : AuthApi {
    override val isLoggedIn: Flow<Boolean> = store.state.map { it.isLoggedIn }.distinctUntilChanged()
}
