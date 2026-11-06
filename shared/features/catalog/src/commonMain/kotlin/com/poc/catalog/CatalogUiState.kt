package com.poc.catalog

import com.poc.catalog.api.ProductSummary

data class CatalogUiState(
    val products: List<ProductSummary> = emptyList(),
    val isLoading: Boolean = false,
)
