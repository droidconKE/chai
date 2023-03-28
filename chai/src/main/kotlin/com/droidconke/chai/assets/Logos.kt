package com.droidconke.chai.assets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Suppress("ComposableNaming")
object Logo {
    private val provider: AssetProvider by lazy { AssetProvider }

    @Composable
    fun DCKE(): ImageBitmap = provider.image(path = "logo/logo_dcke.png")
}