package com.droidconke.chai.assets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import com.droidconke.chai.exceptions.attemptAssetRetrieval
import java.io.InputStream

object AssetProvider {

    fun image(ctx: Context, path: String): Bitmap =
        attemptAssetRetrieval {
            val stream: InputStream = ctx.assets.open(path)
            val bitmap = BitmapDrawable.createFromStream(stream, null)?.toBitmap()
            stream.close()
            return@attemptAssetRetrieval bitmap ?: error("Asset can't be null")
        }
}
