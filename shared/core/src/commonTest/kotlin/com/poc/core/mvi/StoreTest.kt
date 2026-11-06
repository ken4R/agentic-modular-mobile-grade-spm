package com.poc.core.mvi

import kotlin.test.Test
import kotlin.test.assertEquals

class StoreTest {
    private class CounterStore : Store<Int, Int>(initialState = 0) {
        override fun dispatch(intent: Int) = reduce { this + intent }
    }

    @Test
    fun dispatchReducesState() {
        val store = CounterStore()
        store.dispatch(2)
        store.dispatch(3)
        assertEquals(5, store.state.value)
        store.dispose()
    }
}
