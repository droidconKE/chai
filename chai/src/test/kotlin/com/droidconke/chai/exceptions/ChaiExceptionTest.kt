package com.droidconke.chai.exceptions

import org.junit.Assert.assertEquals
import org.junit.Test

class ChaiExceptionTest {

    @Test
    fun `test chai exception`() {
        val message = "test message"
        val exception = ChaiException(message)

        assertEquals(message, exception.message)
    }
}