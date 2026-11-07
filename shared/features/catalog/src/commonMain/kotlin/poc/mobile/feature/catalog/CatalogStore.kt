package poc.mobile.feature.catalog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import poc.mobile.core.Outcome
import poc.mobile.core.mvi.BaseStore
import kotlin.coroutines.CoroutineContext

class CatalogStore(
    private val repository: CatalogRepository,
    context: CoroutineContext,
) : BaseStore(context) {
    private val mutableState = MutableStateFlow(CatalogUiState.Initial)
    val state: StateFlow<CatalogUiState> = mutableState.asStateFlow()

    fun accept(intent: CatalogIntent) {
        when (intent) {
            CatalogIntent.Load -> load()
        }
    }

    private fun load() {
        if (mutableState.value.isLoading) return
        mutableState.update { it.copy(isLoading = true, error = null) }
        scope.launch {
            when (val outcome = repository.products()) {
                is Outcome.Success -> mutableState.update { it.copy(isLoading = false, products = outcome.value) }
                is Outcome.Failure -> mutableState.update { it.copy(isLoading = false, error = outcome.message) }
            }
        }
    }
}
