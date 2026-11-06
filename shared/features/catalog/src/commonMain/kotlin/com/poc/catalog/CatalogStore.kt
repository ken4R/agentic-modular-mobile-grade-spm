package com.poc.catalog

import com.poc.core.mvi.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CatalogStore(
    private val getProducts: GetProductsUseCase,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : Store<CatalogIntent, CatalogUiState>(CatalogUiState(), scope) {

    override fun dispatch(intent: CatalogIntent) {
        when (intent) {
            CatalogIntent.Load -> load()
        }
    }

    private fun load() {
        reduce { copy(isLoading = true) }
        scope.launch {
            val products = getProducts()
            reduce { copy(products = products, isLoading = false) }
        }
    }
}
