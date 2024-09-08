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
package com.droidconke.chai.atoms

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.droidconke.chai.R

/**
 * Chai typography:
 * Consists of 2 files that work together:
 *  - Typography(located in the main package) and
 *  - Font located in the atoms directory
 *
 * Font:
 * Defines the font family types only here
 * We use val for the purpose of making it available in the entire application
 * You first add the fonts to the res folder under fonts
 * Then just reference them here.
 *
 * Font - List all fonts that will be used in the application
 *
 */
internal val MontserratSans: FontFamily
    @Composable
    get() = FontFamily(
        MontserratBold,
        MontserratSemiBold,
        MontserratMedium,
        MontserratRegular,
        MontserratLight,
        MontserratExtraLight,
        MontserratThin
    )

private val MontserratBold = Font(R.font.montserrat_bold, weight = FontWeight.Bold)
private val MontserratSemiBold = Font(R.font.montserrat_semi_bold, weight = FontWeight.SemiBold)
private val MontserratMedium = Font(R.font.montserrat_medium, weight = FontWeight.Medium)
private val MontserratRegular = Font(R.font.montserrat_regular, weight = FontWeight.Normal)
private val MontserratLight = Font(R.font.montserrat_light, weight = FontWeight.Light)
private val MontserratExtraLight = Font(R.font.montserrat_extra_light, weight = FontWeight.ExtraLight)
private val MontserratThin = Font(R.font.montserrat_thin, weight = FontWeight.Thin)
