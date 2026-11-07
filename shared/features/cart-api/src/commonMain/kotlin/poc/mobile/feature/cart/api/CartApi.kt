package poc.mobile.feature.cart.api

import kotlinx.coroutines.flow.Flow

/** Contrat public de la feature cart, consommable par les autres features. */
interface CartApi {
    /** Nombre total d'articles dans le panier (quantités cumulées). */
    val itemCount: Flow<Int>
}

object CartRoute {
    const val PATH = "cart"
}
