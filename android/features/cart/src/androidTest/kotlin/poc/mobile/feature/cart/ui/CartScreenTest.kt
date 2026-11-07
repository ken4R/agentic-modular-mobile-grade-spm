package poc.mobile.feature.cart.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import org.junit.Rule
import org.junit.Test
import poc.mobile.core.Outcome
import poc.mobile.feature.cart.CartLine
import poc.mobile.feature.cart.CartRepository
import poc.mobile.feature.cart.CartStore

class CartScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun emptyCartShowsPlaceholder() {
        compose.setContent { CartScreen(CartStore(FakeCartRepository(), Dispatchers.Main)) }
        compose.onNodeWithTag(CartTags.EMPTY).assertIsDisplayed()
    }

    @Test
    fun linesAreListedAndClearEmptiesCart() {
        val repository = FakeCartRepository(listOf(CartLine("p1", "Café", 450, 2)))
        compose.setContent { CartScreen(CartStore(repository, Dispatchers.Main)) }
        compose.onNodeWithTag(CartTags.line("p1")).assertIsDisplayed()
        compose.onNodeWithTag(CartTags.TOTAL).assertIsDisplayed()
        compose.onNodeWithTag(CartTags.CLEAR).performClick()
        compose.onNodeWithTag(CartTags.EMPTY).assertIsDisplayed()
    }

    private class FakeCartRepository(initial: List<CartLine> = emptyList()) : CartRepository {
        private val mutableLines = MutableStateFlow(initial)
        override val lines: StateFlow<List<CartLine>> = mutableLines
        override val itemCount: Flow<Int> = mutableLines.map { it.sumOf(CartLine::quantity) }
        override suspend fun add(productId: String): Outcome<Unit> = Outcome.Failure("non utilisé")
        override suspend fun decrement(productId: String) = Unit
        override suspend fun remove(productId: String) = Unit
        override suspend fun clear() {
            mutableLines.value = emptyList()
        }
    }
}
