package poc.mobile.feature.auth

data class AuthUiState(
    val email: String,
    val password: String,
    val isLoading: Boolean,
    val isAuthenticated: Boolean,
    val error: String?,
) {
    companion object {
        val Initial = AuthUiState(email = "", password = "", isLoading = false, isAuthenticated = false, error = null)
    }
}
