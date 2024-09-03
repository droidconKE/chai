/*
 * Copyright 2022 DroidconKE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.droidconke.chai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.droidconke.chai.ChaiTheme
import com.droidconke.chai.colors.LocalChaiColorsPalette
import com.droidconke.chai.typography.LocalChaiTypography
import com.droidconke.chai.utils.ChaiPreview

/**
 * CText:
 * Typography( From  is the art of arranging letters and text in a way that makes the copy legible,
 * clear, and visually appealing to the reader.
 * It involves font style, appearance, and structure, which aims to elicit certain emotions and convey specific messages.
 * In short, typography is what brings the text to life.
 *
 * This is a shorter approach where our theme will not require a specific font BUT will use CText as a file to construct
 * our text. this is a shorter approach for making a Design system type. For a longer version see this repo:
 * [KahawaLove](https://github.com/tamzi/KahawaLove)
 *
 * */
@Composable
fun ChaiTextDisplay(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.header,
        color = color
    )
}

@Composable
fun ChaiTextTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.title,
        color = color
    )
}

@Composable
fun ChaiTextHeader(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.header,
        color = color
    )
}

@Composable
fun ChaiTextBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.body,
        color = color
    )
}

@Composable
internal fun ChaiTextButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.button,
        color = color,
    )
}

@Composable
fun ChaiTextLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalChaiColorsPalette.current.textNormalColor
) {
    Text(
        modifier = modifier,
        text = text,
        style = LocalChaiTypography.current.label,
        color = color
    )
}

@ChaiPreview
@Composable
fun ChaiTextsPreview() {
    ChaiTheme {
        Column(
            modifier = Modifier
                .background(LocalChaiColorsPalette.current.surfaces)
                .padding(16.dp)
        ) {
            ChaiTextDisplay(text = "ChaiTextDisplay")
            ChaiTextTitle(text = "ChaiTextTitle")
            ChaiTextHeader(text = "ChaiTextHeader")
            ChaiTextBody(text = "ChaiTextBody")
            ChaiTextButton(text = "ChaiTextButton")
            ChaiTextLabel(text = "ChaiTextLabel")
        }
    }
}
