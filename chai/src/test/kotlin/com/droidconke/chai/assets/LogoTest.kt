package com.droidconke.chai.assets

import io.github.hellocuriosity.compose.test.ComposeTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class LogoTest : ComposeTest() {

    @Test
    fun testImageBitmapExists() {
        composeTestRule.setContent {
            assertNotNull(Logo.DCKE())
        }
    }
}
