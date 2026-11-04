package poc.mobile.feature.auth.api

import kotlinx.coroutines.flow.StateFlow

/** Contrat public de la feature auth, consommable par les autres features. */
interface AuthApi {
    /** Session courante, `null` si l'utilisateur n'est pas connecté. */
    val session: StateFlow<Session?>
}

data class Session(val email: String)

object AuthRoute {
    const val PATH = "auth"
}
