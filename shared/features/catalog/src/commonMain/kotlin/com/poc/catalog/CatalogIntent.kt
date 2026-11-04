package com.poc.catalog

sealed interface CatalogIntent {
    data object Load : CatalogIntent
}
