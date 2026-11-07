package poc.mobile.core

import kotlin.test.Test
import kotlin.test.assertEquals

class PricesTest {
    @Test
    fun formatsCentsAsEuros() {
        assertEquals("4,50 €", Prices.format(450))
        assertEquals("0,05 €", Prices.format(5))
        assertEquals("12,00 €", Prices.format(1200))
    }
}
