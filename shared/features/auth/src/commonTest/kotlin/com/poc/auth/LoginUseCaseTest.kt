package com.poc.auth

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class LoginUseCaseTest {
    private val useCase = LoginUseCase(FakeAuthRepository())

    @Test
    fun validEmailSucceeds() = runTest {
        assertTrue(useCase(" user@poc.dev ").isSuccess)
    }

    @Test
    fun blankEmailFails() = runTest {
        assertTrue(useCase("   ").isFailure)
    }

    @Test
    fun emailWithoutAtFails() = runTest {
        assertTrue(useCase("user").isFailure)
    }
}
