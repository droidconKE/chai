package com.droidconke.chai.assets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Suppress("ComposableNaming")
object Images {
    private val provider: AssetProvider = AssetProvider

    @Composable
    fun DCKE_MAIN(): ImageBitmap =
        provider.image(path = "image/image_dckemain.png")
}