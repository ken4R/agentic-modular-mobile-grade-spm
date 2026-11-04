package com.poc.auth

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String): Result<Unit> {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return Result.failure(IllegalArgumentException("Adresse vide"))
        return repository.login(trimmed)
    }
}
