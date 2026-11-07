package poc.mobile.feature.catalog

import poc.mobile.feature.catalog.api.ProductSummary

data class CatalogUiState(
    val products: List<ProductSummary>,
    val isLoading: Boolean,
    val error: String?,
) {
    companion object {
        val Initial = CatalogUiState(products = emptyList(), isLoading = false, error = null)
    }
}
