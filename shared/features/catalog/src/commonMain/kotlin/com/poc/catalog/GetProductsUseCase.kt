package com.poc.catalog

import com.poc.catalog.api.ProductSummary

class GetProductsUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(): List<ProductSummary> = repository.products().sortedBy { it.name }
}
