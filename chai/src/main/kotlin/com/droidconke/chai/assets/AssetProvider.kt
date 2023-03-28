package com.droidconke.chai.assets

import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.droidconke.chai.exceptions.attemptAssetRetrieval
import java.io.InputStream

object AssetProvider {
    @Composable
    fun image(path: String): ImageBitmap {
        val ctx = LocalContext.current
        val bitmap = remember(path) {
            attemptAssetRetrieval {
                val stream: InputStream = ctx.assets.open(path)
                val bitmap =
                    BitmapDrawable
                        .createFromStream(stream, null)
                        ?.toBitmap()
                        ?.asImageBitmap()
                stream.close()
                return@attemptAssetRetrieval bitmap ?: error("Asset can't be null")
            }
        }

        // Prevent memory leaks when composable view is disposed
        DisposableEffect(bitmap) {
            onDispose {
                bitmap.asAndroidBitmap().recycle()
            }
        }

        return bitmap
    }
}
