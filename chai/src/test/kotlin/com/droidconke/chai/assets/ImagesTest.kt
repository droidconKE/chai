package com.droidconke.chai.assets

import io.github.hellocuriosity.compose.test.ComposeTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class ImagesTest : ComposeTest() {

    @Test
    fun testImageBitmapExists() {
        composeTestRule.setContent {
            assertNotNull(Images.DCKE_BANNER())
            assertNotNull(Images.DCKE_MAIN())
            assertNotNull(Images.DCKE_SMILING())
            assertNotNull(Images.DCKE_SESSION())
            assertNotNull(Images.DCKE_TEAM())
        }
    }
}
