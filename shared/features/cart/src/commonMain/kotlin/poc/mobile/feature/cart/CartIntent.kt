package poc.mobile.feature.cart

sealed interface CartIntent {
    /** Ajoute une unité du produit (le crée dans le panier s'il n'y est pas). */
    data class Add(val productId: String) : CartIntent
    /** Retire une unité ; la ligne disparaît à zéro. */
    data class Decrement(val productId: String) : CartIntent
    data class Remove(val productId: String) : CartIntent
    data object Clear : CartIntent
}
