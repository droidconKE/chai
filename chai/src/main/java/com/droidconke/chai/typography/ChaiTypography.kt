package com.droidconke.chai.typography

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.droidconke.chai.ChaiTheme
import com.droidconke.chai.utils.ChaiPreview

private val DUMMY_CHAI_TEXT = "Design systems provide guidelines and reusable components to ensure consistent and efficient design across digital products, improving user experience and streamlining team workflows."

private val ChaiDisplayTextStyle = TextStyle(
    fontSize = 45.sp,
    fontWeight = FontWeight.Bold,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.25.em
)
@ChaiPreview
@Composable
private fun ChaiDisplayTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiDisplayTextStyle)
    }
}

private val ChaiTitleTextStyle = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.25.em
)
@ChaiPreview
@Composable
private fun ChaiTitleTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiTitleTextStyle)
    }
}

private val ChaiHeaderTextStyle = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.SemiBold,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.2.em,
)
@ChaiPreview
@Composable
private fun ChaiHeaderTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiHeaderTextStyle)
    }
}

private val ChaiButtonTextStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.1.em,
)
@ChaiPreview
@Composable
private fun ChaiButtonTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiButtonTextStyle)
    }
}

private val ChaiBodyTextStyle = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.1.em,
)
@ChaiPreview
@Composable
private fun ChaiBodyTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiBodyTextStyle)
    }
}

private val ChaiLabelTextStyle = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Bold,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineHeight = 1.1.em,
)
@ChaiPreview
@Composable
private fun ChaiLabelTextPreview(){
    ChaiTheme {
        Text(text = DUMMY_CHAI_TEXT, style = ChaiLabelTextStyle)
    }
}

data class ChaiTypography(
    val display: TextStyle = ChaiDisplayTextStyle,
    val title: TextStyle = ChaiTitleTextStyle,
    val header: TextStyle = ChaiHeaderTextStyle,
    val button: TextStyle = ChaiButtonTextStyle,
    val body: TextStyle = ChaiBodyTextStyle,
    val label: TextStyle = ChaiLabelTextStyle
) {
    companion object {
        val Default = ChaiBodyTextStyle
    }
}

@Composable
fun chaiTypography(
    display: TextStyle = ChaiDisplayTextStyle,
    title: TextStyle = ChaiTitleTextStyle,
    header: TextStyle = ChaiHeaderTextStyle,
    button: TextStyle = ChaiButtonTextStyle,
    body: TextStyle = ChaiBodyTextStyle,
    label: TextStyle = ChaiLabelTextStyle,
) = ChaiTypography(
    display = display,
    title = title,
    header = header,
    button = button,
    body = body,
    label = label
)

internal val LocalChaiTypography: ProvidableCompositionLocal<ChaiTypography>
    get() = compositionLocalOf { ChaiTypography() }
