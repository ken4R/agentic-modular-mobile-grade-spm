package com.poc.catalog

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetProductsUseCaseTest {
    @Test
    fun productsAreSortedByName() = runTest {
        val names = GetProductsUseCase(FakeCatalogRepository())().map { it.name }
        assertEquals(names.sorted(), names)
    }
}
