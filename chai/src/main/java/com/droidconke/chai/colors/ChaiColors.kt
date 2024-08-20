/*
 * Copyright 2023 DroidconKE
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
package com.droidconke.chai.colors

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.droidconke.chai.atoms.ChaiBlack
import com.droidconke.chai.atoms.ChaiBlue
import com.droidconke.chai.atoms.ChaiCoal
import com.droidconke.chai.atoms.ChaiDarkGrey
import com.droidconke.chai.atoms.ChaiGrey
import com.droidconke.chai.atoms.ChaiSteal
import com.droidconke.chai.atoms.ChaiSilver
import com.droidconke.chai.atoms.ChaiLightGrey
import com.droidconke.chai.atoms.ChaiOrange
import com.droidconke.chai.atoms.ChaiSubtleGrey
import com.droidconke.chai.atoms.ChaiDarkerGrey
import com.droidconke.chai.atoms.ChaiTeal
import com.droidconke.chai.atoms.ChaiTealLight
import com.droidconke.chai.atoms.ChaiWhite
/**
 * This class has the semantic names of the Chai design system colors.
 *
 * What are semantic names you might ask:
 * * Semantic names are a consistent way to refer to colors based on their purpose in the UI.
 * * They define how a primitive color(the individual color such as ChaiBlue) will be used throughout a design system
 * */
@Immutable
data class ChaiColors(
    val primary: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val surfaces: Color = Color.Unspecified,
    val cardsBackground: Color = Color.Unspecified,
    val cardsBorderColor: Color = Color.Unspecified,
    val bottomNavBorderColor: Color = Color.Unspecified,
    val activeBottomNavIconColor: Color = Color.Unspecified,
    val activeBottomNavTextColor: Color = Color.Unspecified,
    val inactiveBottomNavIconColor: Color = Color.Unspecified,
    val bottomNavBackgroundColor: Color = Color.Unspecified,
    val textTitlePrimaryColor: Color = Color.Unspecified,
    val textBoldColor: Color = Color.Unspecified,
    val textNormalColor: Color = Color.Unspecified,
    val textWeakColor: Color = Color.Unspecified,
    val textLabelAndHeadings: Color = Color.Unspecified,
    val linkTextColorPrimary: Color = Color.Unspecified,
    val secondaryButtonColor: Color = Color.Unspecified,
    val secondaryButtonTextColor: Color = Color.Unspecified,
    val outlinedButtonBackgroundColor: Color = Color.Unspecified,
    val outlinedButtonTextColor: Color = Color.Unspecified,
    val textButtonColor: Color = Color.Unspecified,
    val radioButtonColors: Color = Color.Unspecified,
    val toggleOffBackgroundColor: Color = Color.Unspecified,
    val toggleOffIconBackgroundColor: Color = Color.Unspecified,
    val toggleOffIconColor: Color = Color.Unspecified,
    val toggleOnBackgroundColor: Color = Color.Unspecified,
    val toggleOnIconBackgroundColor: Color = Color.Unspecified,
    val toggleOnIconColor: Color = Color.Unspecified,
    val loadingStateOnCardsColor: Color = Color.Unspecified,
    val eventDaySelectorActiveSurfaceColor: Color = Color.Unspecified,
    val eventDaySelectorInactiveSurfaceColor: Color = Color.Unspecified,
    val eventDaySelectorInactiveTextColor: Color = Color.Unspecified,
    val eventDaySelectorActiveTextColor: Color = Color.Unspecified,
    val badgeBackgroundColor: Color = Color.Unspecified,
    val textFieldBackgroundColor: Color = Color.Unspecified,
    val textFieldBorderColor: Color = Color.Unspecified,
    val bottomSheetBackgroundColor: Color = Color.Unspecified,
    val inactiveMultiSelectButtonBorderColor: Color = Color.Unspecified
)

val LocalChaiColorsPalette = staticCompositionLocalOf { ChaiColors() }

val ChaiLightColorPalette = ChaiColors(
    primary = ChaiBlue,
    background = ChaiWhite,
    surfaces = ChaiSilver,
    cardsBackground = ChaiWhite,
    cardsBorderColor = ChaiSilver,
    bottomNavBorderColor = ChaiSilver,
    activeBottomNavIconColor = ChaiBlue,
    inactiveBottomNavIconColor = ChaiSteal,
    bottomNavBackgroundColor = ChaiWhite,
    activeBottomNavTextColor = ChaiOrange,
    textTitlePrimaryColor = ChaiBlue,
    textBoldColor = ChaiSteal,
    textNormalColor = ChaiSteal,
    textWeakColor = ChaiSubtleGrey,
    textLabelAndHeadings = ChaiBlue,
    linkTextColorPrimary = ChaiBlue,
    secondaryButtonColor = ChaiBlue,
    secondaryButtonTextColor = ChaiLightGrey,
    outlinedButtonBackgroundColor = ChaiWhite,
    outlinedButtonTextColor = ChaiCoal,
    textButtonColor = ChaiBlue,
    radioButtonColors = ChaiSubtleGrey,
    toggleOffBackgroundColor = ChaiSteal,
    toggleOffIconBackgroundColor = ChaiWhite,
    toggleOffIconColor = ChaiSteal,
    toggleOnBackgroundColor = ChaiOrange,
    toggleOnIconBackgroundColor = ChaiWhite,
    toggleOnIconColor = ChaiOrange,
    loadingStateOnCardsColor = ChaiGrey,
    eventDaySelectorActiveSurfaceColor = ChaiOrange,
    eventDaySelectorInactiveSurfaceColor = ChaiTealLight,
    eventDaySelectorActiveTextColor = ChaiWhite,
    eventDaySelectorInactiveTextColor = ChaiSteal,
    badgeBackgroundColor = ChaiCoal,
    textFieldBackgroundColor = ChaiSilver,
    textFieldBorderColor = ChaiSilver,
    bottomSheetBackgroundColor = ChaiLightGrey,
    inactiveMultiSelectButtonBorderColor = ChaiSteal
)

val ChaiDarkColorPalette = ChaiColors(
    primary = ChaiBlack,
    background = ChaiSteal,
    surfaces = ChaiBlack,
    cardsBackground = ChaiBlack,
    cardsBorderColor = ChaiDarkerGrey,
    bottomNavBorderColor = ChaiSteal,
    activeBottomNavIconColor = ChaiTeal,
    inactiveBottomNavIconColor = ChaiWhite,
    bottomNavBackgroundColor = ChaiBlack,
    activeBottomNavTextColor = ChaiOrange,
    textTitlePrimaryColor = ChaiWhite,
    textBoldColor = ChaiSilver,
    textNormalColor = ChaiWhite,
    textWeakColor = ChaiGrey,
    textLabelAndHeadings = ChaiTealLight,
    linkTextColorPrimary = ChaiSilver,
    secondaryButtonColor = ChaiTealLight,
    secondaryButtonTextColor = ChaiSteal,
    outlinedButtonBackgroundColor = ChaiBlack,
    outlinedButtonTextColor = ChaiTealLight,
    textButtonColor = ChaiSilver,
    radioButtonColors = ChaiWhite,
    toggleOffBackgroundColor = ChaiSilver,
    toggleOffIconBackgroundColor = ChaiWhite,
    toggleOffIconColor = ChaiSteal,
    toggleOnBackgroundColor = ChaiOrange,
    toggleOnIconBackgroundColor = ChaiWhite,
    toggleOnIconColor = ChaiOrange,
    loadingStateOnCardsColor = ChaiDarkGrey,
    eventDaySelectorActiveSurfaceColor = ChaiOrange,
    eventDaySelectorInactiveSurfaceColor = ChaiTealLight,
    eventDaySelectorActiveTextColor = ChaiWhite,
    eventDaySelectorInactiveTextColor = ChaiWhite,
    badgeBackgroundColor = ChaiBlack,
    textFieldBackgroundColor = ChaiSteal,
    textFieldBorderColor = ChaiSubtleGrey,
    bottomSheetBackgroundColor = ChaiBlack,
    inactiveMultiSelectButtonBorderColor = ChaiGrey
)