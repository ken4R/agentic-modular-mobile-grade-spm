package com.poc.auth

data class AuthUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
)
