package com.poc.auth

sealed interface AuthIntent {
    data class EmailChanged(val value: String) : AuthIntent
    data object Submit : AuthIntent
    data object Logout : AuthIntent
}
