package poc.mobile.feature.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import poc.mobile.core.Outcome
import poc.mobile.feature.auth.api.AuthApi
import poc.mobile.feature.auth.api.Session

interface AuthRepository : AuthApi {
    suspend fun login(email: String, password: String): Outcome<Session>
    suspend fun logout()
}

/** Pas de backend dans le POC : identifiants validés localement. */
internal class InMemoryAuthRepository : AuthRepository {
    private val mutableSession = MutableStateFlow<Session?>(null)
    override val session: StateFlow<Session?> = mutableSession.asStateFlow()

    override suspend fun login(email: String, password: String): Outcome<Session> = when {
        !email.contains('@') -> Outcome.Failure("Adresse e-mail invalide")
        password.length < MIN_PASSWORD_LENGTH -> Outcome.Failure("Mot de passe trop court")
        else -> Session(email).also { mutableSession.value = it }.let { Outcome.Success(it) }
    }

    override suspend fun logout() {
        mutableSession.value = null
    }

    private companion object {
        const val MIN_PASSWORD_LENGTH = 4
    }
}
