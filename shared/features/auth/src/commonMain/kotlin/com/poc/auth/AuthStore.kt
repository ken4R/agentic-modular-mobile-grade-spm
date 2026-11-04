package com.poc.auth

import com.poc.core.mvi.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AuthStore(
    private val login: LoginUseCase,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : Store<AuthIntent, AuthUiState>(AuthUiState(), scope) {

    override fun dispatch(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.EmailChanged -> reduce { copy(email = intent.value, error = null) }
            AuthIntent.Submit -> submit()
            AuthIntent.Logout -> reduce { AuthUiState() }
        }
    }

    private fun submit() {
        val email = state.value.email
        reduce { copy(isLoading = true, error = null) }
        scope.launch {
            login(email).fold(
                onSuccess = { reduce { copy(isLoading = false, isLoggedIn = true) } },
                onFailure = { e -> reduce { copy(isLoading = false, error = e.message) } },
            )
        }
    }
}
