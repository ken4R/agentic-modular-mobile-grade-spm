package poc.mobile.feature.auth.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test
import poc.mobile.core.Outcome
import poc.mobile.feature.auth.AuthRepository
import poc.mobile.feature.auth.AuthStore
import poc.mobile.feature.auth.api.Session

class AuthScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun successfulLoginShowsWelcome() {
        val store = AuthStore(FakeAuthRepository(Outcome.Success(Session("a@b.c"))), Dispatchers.Main)
        compose.setContent { AuthScreen(store) }
        compose.onNodeWithTag(AuthTags.EMAIL).performTextInput("a@b.c")
        compose.onNodeWithTag(AuthTags.PASSWORD).performTextInput("secret")
        compose.onNodeWithTag(AuthTags.SUBMIT).performClick()
        compose.onNodeWithTag(AuthTags.WELCOME).assertIsDisplayed()
    }

    @Test
    fun failedLoginShowsError() {
        val store = AuthStore(FakeAuthRepository(Outcome.Failure("refusé")), Dispatchers.Main)
        compose.setContent { AuthScreen(store) }
        compose.onNodeWithTag(AuthTags.SUBMIT).performClick()
        compose.onNodeWithTag(AuthTags.ERROR).assertIsDisplayed()
    }

    private class FakeAuthRepository(private val result: Outcome<Session>) : AuthRepository {
        override val session: StateFlow<Session?> = MutableStateFlow(null)
        override suspend fun login(email: String, password: String): Outcome<Session> = result
        override suspend fun logout() = Unit
    }
}
