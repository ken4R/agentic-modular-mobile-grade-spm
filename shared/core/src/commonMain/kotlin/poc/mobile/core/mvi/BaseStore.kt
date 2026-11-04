package poc.mobile.core.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 * Socle des Stores MVI. Chaque Store déclare lui-même, avec ses types concrets (lisibles depuis Swift) :
 * `val state: StateFlow<XUiState>` et `fun accept(intent: XIntent)`.
 */
abstract class BaseStore(context: CoroutineContext) {
    protected val scope: CoroutineScope = CoroutineScope(SupervisorJob() + context)

    /** À appeler quand l'écran propriétaire disparaît. */
    fun close() {
        scope.cancel()
    }
}
