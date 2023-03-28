package com.droidconke.chai.assets

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Suppress("ComposableNaming")
object Images {
    private val provider: AssetProvider by lazy { AssetProvider }

    @Composable
    fun DCKE_BANNER(): ImageBitmap = provider.image(path = "image/image_dcke_event_banner.png")

    @Composable
    fun DCKE_MAIN(): ImageBitmap = provider.image(path = "image/image_dcke_main.png")

    @Composable
    fun DCKE_SMILING(): ImageBitmap = provider.image(path = "image/image_smiling.png")

    @Composable
    fun DCKE_SESSION(): ImageBitmap =
        provider.image(path = "image/image_session_transforming_lives.png")

    @Composable
    fun DCKE_TEAM(): ImageBitmap = provider.image(path = "image/image_dcke_team.png")
}