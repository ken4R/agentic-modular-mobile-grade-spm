package poc.mobile.feature.catalog.api

/** Contrat public de la feature catalog, consommable par les autres features (ex. cart). */
interface CatalogApi {
    /** Produit du catalogue, `null` s'il n'existe pas. */
    suspend fun findProduct(id: String): ProductSummary?
}

data class ProductSummary(val id: String, val name: String, val priceCents: Long) {
    val priceLabel: String get() = Prices.format(priceCents)
}

/** Formatage partagé des montants (Android et iOS affichent le même libellé). */
object Prices {
    private const val CENTS_PER_EURO = 100

    fun format(cents: Long): String {
        val euros = cents / CENTS_PER_EURO
        val rest = (cents % CENTS_PER_EURO).toString().padStart(2, '0')
        return "$euros,$rest €"
    }
}

object CatalogRoute {
    const val PATH = "catalog"
}
