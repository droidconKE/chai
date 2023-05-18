
package com.droidconke.chai.atoms

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

/**
 * Chai typography:
 * Consists of 2 files that work together:
 *  - CTypography(located ion the components package) and
 *  - CFont located in the atoms directory
 * Font:
 * Defines the font family types only here
 * We use val for the purpose of making it available in the entire application
 * You first add the fonts to the res folder under fonts
 * Then just reference them here.
 * Font-  List all fonts that will be used in the application
 *
 */

val MontserratRegular = FontFamily(Font(R.font.montserrat_regular))
val MontserratBold = FontFamily(Font(R.font.montserrat_bold))
val MontserratSemiBold = FontFamily(Font(R.font.montserrat_semi_bold))
val MontserratLight = FontFamily(Font(R.font.montserrat_light))
val MontserratMedium = FontFamily(Font(R.font.montserrat_medium))

val MontserratExtraLight = FontFamily(Font(R.font.montserrat_extra_light))

val MontserratThin = FontFamily(Font(R.font.montserrat_thin))