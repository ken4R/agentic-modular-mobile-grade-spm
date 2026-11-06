package com.poc.core.format

private const val CENTS_PER_EURO = 100

/** Formate un prix en centimes pour l'affichage : 450 → « 4,50 € ». */
fun formatPrice(cents: Long): String {
    val euros = cents / CENTS_PER_EURO
    val rest = (cents % CENTS_PER_EURO).toString().padStart(2, '0')
    return "$euros,$rest €"
}
