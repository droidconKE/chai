package com.droidconke.chai.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Chai Light Preview", uiMode = Configuration.UI_MODE_NIGHT_NO)
private annotation class ChaiPreviewLight

@Preview(name = "Chai Dark Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
private annotation class ChaiPreviewDark

@ChaiPreviewLight
@ChaiPreviewDark
annotation class ChaiPreview