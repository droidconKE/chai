package com.droidconke.chai.assets

import android.content.Context
import android.graphics.Bitmap
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class ChaiImageTest(private val bitmap: Bitmap) {

    companion object {
        private val ctx: Context = ApplicationProvider.getApplicationContext()

        @JvmStatic
        @Parameters
        fun data() = listOf(
            arrayOf(ChaiImage.DCKE_MAIN(ctx)),
        )
    }

    @Test
    fun testBitmapExists() {
        assertNotNull(bitmap)
    }
}