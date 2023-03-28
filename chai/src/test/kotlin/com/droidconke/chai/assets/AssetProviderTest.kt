package com.droidconke.chai.assets

import com.droidconke.chai.exceptions.ChaiException
import io.github.hellocuriosity.compose.test.ComposeTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class AssetProviderTest : ComposeTest() {
    private val provider = AssetProvider
    private val image = "image_dckemain.png"

    @Test
    fun testImage() {
        composeTestRule.setContent {
            val bitmap = provider.image(image)
            assertNotNull(bitmap)
        }
    }

    @Test(expected = ChaiException::class)
    fun testImageFileNotFound() {
        composeTestRule.setContent {
            provider.image("non_existent_image.png")
        }
    }
}
