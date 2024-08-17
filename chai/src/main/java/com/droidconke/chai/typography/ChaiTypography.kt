package com.droidconke.chai.typography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val ChaiDisplayTextStyle = TextStyle(fontSize = 45.sp)
private val ChaiTitleTextStyle = TextStyle(fontSize = 32.sp)
private val ChaiHeaderTextStyle = TextStyle(fontSize = 22.sp)
private val ChaiParagraphTextStyle = TextStyle(fontSize = 16.sp)
private val ChaiBodyTextStyle = TextStyle(fontSize = 14.sp)
private val ChaiLabelTextStyle = TextStyle(fontSize = 12.sp)

data class ChaiTypography(
    val display: TextStyle = ChaiDisplayTextStyle,
    val title: TextStyle = ChaiTitleTextStyle,
    val header: TextStyle = ChaiHeaderTextStyle,
    val paragraph: TextStyle = ChaiParagraphTextStyle,
    val body: TextStyle = ChaiBodyTextStyle,
    val label: TextStyle = ChaiLabelTextStyle
){
    companion object {
        val Default = ChaiBodyTextStyle
    }
}

@Composable
fun chaiTypography(
    display: TextStyle = ChaiDisplayTextStyle,
    title: TextStyle = ChaiTitleTextStyle,
    header: TextStyle = ChaiHeaderTextStyle,
    paragraph: TextStyle = ChaiParagraphTextStyle,
    body: TextStyle = ChaiBodyTextStyle,
    label: TextStyle = ChaiLabelTextStyle,
) = ChaiTypography(
    display = display,
    title = title,
    header = header,
    paragraph = paragraph,
    body = body,
    label = label
)

internal val LocalChaiTypography: ProvidableCompositionLocal<ChaiTypography>
    get() = compositionLocalOf { ChaiTypography() }
