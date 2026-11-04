package poc.mobile.feature.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import poc.mobile.core.Outcome
import poc.mobile.core.mvi.BaseStore
import kotlin.coroutines.CoroutineContext

class AuthStore(
    private val repository: AuthRepository,
    context: CoroutineContext,
) : BaseStore(context) {
    private val mutableState = MutableStateFlow(AuthUiState.Initial)
    val state: StateFlow<AuthUiState> = mutableState.asStateFlow()

    fun accept(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.EmailChanged -> mutableState.update { it.copy(email = intent.value, error = null) }
            is AuthIntent.PasswordChanged -> mutableState.update { it.copy(password = intent.value, error = null) }
            AuthIntent.Submit -> submit()
            AuthIntent.Logout -> logout()
        }
    }

    private fun submit() {
        val current = mutableState.value
        if (current.isLoading) return
        mutableState.update { it.copy(isLoading = true, error = null) }
        scope.launch {
            when (val outcome = repository.login(current.email, current.password)) {
                is Outcome.Success -> mutableState.update { it.copy(isLoading = false, isAuthenticated = true) }
                is Outcome.Failure -> mutableState.update { it.copy(isLoading = false, error = outcome.message) }
            }
        }
    }

    private fun logout() {
        scope.launch {
            repository.logout()
            mutableState.value = AuthUiState.Initial
        }
    }
}
