package com.poc.core.format

import kotlin.test.Test
import kotlin.test.assertEquals

class PriceTest {
    @Test
    fun formatsCentsWithTwoDigits() {
        assertEquals("4,50 €", formatPrice(450))
        assertEquals("1,05 €", formatPrice(105))
        assertEquals("0,00 €", formatPrice(0))
    }
}
