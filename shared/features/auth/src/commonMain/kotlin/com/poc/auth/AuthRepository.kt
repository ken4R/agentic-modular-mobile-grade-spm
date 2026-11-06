package com.poc.auth

interface AuthRepository {
    suspend fun login(email: String): Result<Unit>
}

/** Implémentation locale en attendant un backend : accepte toute adresse contenant « @ ». */
class FakeAuthRepository : AuthRepository {
    override suspend fun login(email: String): Result<Unit> =
        if ('@' in email) Result.success(Unit) else Result.failure(IllegalArgumentException("Adresse invalide"))
}
