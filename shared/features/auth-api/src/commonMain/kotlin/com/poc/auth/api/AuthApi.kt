package com.poc.auth.api

import kotlinx.coroutines.flow.Flow

/** Contrat public de la feature auth, consommé par les autres features et les apps. */
interface AuthApi {
    val isLoggedIn: Flow<Boolean>
}

object AuthRoute {
    const val PATH = "auth"
}
