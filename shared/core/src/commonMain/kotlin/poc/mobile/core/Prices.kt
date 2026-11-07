package poc.mobile.core

/** Formatage partagé des montants (Android et iOS affichent le même libellé). */
object Prices {
    private const val CENTS_PER_EURO = 100

    fun format(cents: Long): String {
        val euros = cents / CENTS_PER_EURO
        val rest = (cents % CENTS_PER_EURO).toString().padStart(2, '0')
        return "$euros,$rest €"
    }
}
