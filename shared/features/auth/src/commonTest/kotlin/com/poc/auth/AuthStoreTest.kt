package com.poc.auth

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AuthStoreTest {
    private fun TestScope.store() = AuthStore(LoginUseCase(FakeAuthRepository()), backgroundScope)

    @Test
    fun emailChangedUpdatesState() = runTest {
        val store = store()
        store.dispatch(AuthIntent.EmailChanged("a@b.c"))
        assertEquals("a@b.c", store.state.value.email)
    }

    @Test
    fun submitWithValidEmailLogsIn() = runTest {
        val store = store()
        store.dispatch(AuthIntent.EmailChanged("a@b.c"))
        store.dispatch(AuthIntent.Submit)
        advanceUntilIdle()
        assertTrue(store.state.value.isLoggedIn)
        assertFalse(store.state.value.isLoading)
    }

    @Test
    fun submitWithInvalidEmailShowsError() = runTest {
        val store = store()
        store.dispatch(AuthIntent.EmailChanged("invalid"))
        store.dispatch(AuthIntent.Submit)
        advanceUntilIdle()
        assertFalse(store.state.value.isLoggedIn)
        assertNotNull(store.state.value.error)
    }

    @Test
    fun logoutResetsState() = runTest {
        val store = store()
        store.dispatch(AuthIntent.EmailChanged("a@b.c"))
        store.dispatch(AuthIntent.Submit)
        advanceUntilIdle()
        store.dispatch(AuthIntent.Logout)
        assertEquals(AuthUiState(), store.state.value)
    }
}
