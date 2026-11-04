package poc.mobile.core.mvi

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertTrue

class BaseStoreTest {
    private class ProbeStore(context: CoroutineContext) : BaseStore(context) {
        val job = scope.launch { awaitCancellation() }
    }

    @Test
    fun closeCancelsRunningWork() = runTest {
        val store = ProbeStore(StandardTestDispatcher(testScheduler))
        store.close()
        assertTrue(store.job.isCancelled)
    }
}
