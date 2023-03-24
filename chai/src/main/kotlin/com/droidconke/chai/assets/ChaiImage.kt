package com.droidconke.chai.assets

import android.content.Context
import android.graphics.Bitmap

object ChaiImage {
    val DCKE_MAIN: (Context) -> Bitmap
        get() = { ctx: Context ->
            var bitmap: Bitmap? = null
            if (bitmap == null) {
                bitmap = AssetProvider.image(ctx = ctx, path = "image/image_dckemain.png")
            }
            bitmap
        }
}
