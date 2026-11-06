package com.poc.core.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Store MVI minimal : un état immuable exposé en [StateFlow], modifié uniquement en réponse à un [Intent].
 */
abstract class Store<Intent : Any, State : Any>(
    initialState: State,
    protected val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) {
    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    abstract fun dispatch(intent: Intent)

    protected fun reduce(reducer: State.() -> State) {
        mutableState.update(reducer)
    }

    fun dispose() {
        scope.cancel()
    }
}
