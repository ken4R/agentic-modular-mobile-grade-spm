package poc.mobile.feature.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthStoreTest {
    private val repository = InMemoryAuthRepository()

    private fun TestScope.store() =
        AuthStore(repository, UnconfinedTestDispatcher(testScheduler))

    @Test
    fun emailChangedUpdatesEmail() = runTest {
        val store = store()
        store.accept(AuthIntent.EmailChanged("a@b.c"))
        assertEquals("a@b.c", store.state.value.email)
    }

    @Test
    fun passwordChangedUpdatesPassword() = runTest {
        val store = store()
        store.accept(AuthIntent.PasswordChanged("secret"))
        assertEquals("secret", store.state.value.password)
    }

    @Test
    fun submitWithValidCredentialsAuthenticates() = runTest {
        val store = store()
        store.accept(AuthIntent.EmailChanged("a@b.c"))
        store.accept(AuthIntent.PasswordChanged("secret"))
        store.accept(AuthIntent.Submit)
        assertTrue(store.state.value.isAuthenticated)
        assertFalse(store.state.value.isLoading)
        assertEquals("a@b.c", repository.session.value?.email)
    }

    @Test
    fun submitWithInvalidEmailShowsError() = runTest {
        val store = store()
        store.accept(AuthIntent.EmailChanged("invalide"))
        store.accept(AuthIntent.PasswordChanged("secret"))
        store.accept(AuthIntent.Submit)
        assertFalse(store.state.value.isAuthenticated)
        assertEquals("Adresse e-mail invalide", store.state.value.error)
    }

    @Test
    fun logoutResetsStateAndSession() = runTest {
        val store = store()
        store.accept(AuthIntent.EmailChanged("a@b.c"))
        store.accept(AuthIntent.PasswordChanged("secret"))
        store.accept(AuthIntent.Submit)
        store.accept(AuthIntent.Logout)
        assertEquals(AuthUiState.Initial, store.state.value)
        assertNull(repository.session.value)
    }
}
