package com.droidconke.chai.assets

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.droidconke.chai.exceptions.ChaiException
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AssetProviderTest {
    private val ctx: Context = ApplicationProvider.getApplicationContext()
    private val provider = AssetProvider
    private val image = "image_dckemain.png"

    @Test
    fun testImage() {
        val bitmap = provider.image(ctx, image)
        assertNotNull(bitmap)
    }

    @Test(expected = ChaiException::class)
    fun testImageFileNotFound() {
        provider.image(ctx, "non_existent_image.png")
    }
}