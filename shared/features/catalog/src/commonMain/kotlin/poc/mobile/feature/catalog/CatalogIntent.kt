package poc.mobile.feature.catalog

sealed interface CatalogIntent {
    /** Charge (ou recharge) la liste des produits. */
    data object Load : CatalogIntent
}
