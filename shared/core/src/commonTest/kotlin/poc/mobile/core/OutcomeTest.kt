package poc.mobile.core

import kotlin.test.Test
import kotlin.test.assertEquals

class OutcomeTest {
    @Test
    fun mapTransformsSuccess() {
        assertEquals(Outcome.Success(4), Outcome.Success(2).map { it * 2 })
    }

    @Test
    fun mapKeepsFailure() {
        val failure: Outcome<Int> = Outcome.Failure("boom")
        assertEquals(failure, failure.map { it * 2 })
    }
}
