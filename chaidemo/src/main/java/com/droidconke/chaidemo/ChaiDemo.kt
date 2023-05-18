
package com.droidconke.chaidemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidconke.chai.ChaiDCKE22Theme
import com.droidconke.chai.atoms.ChaiWhite
import com.droidconke.chai.components.*
import com.droidconke.chai.utils.BreathingSpace13
import com.droidconke.chai.utils.BreathingSpace26
import com.droidconke.chai.utils.SeparatorSpace

@Preview(showBackground = true)
@Composable
fun ChaiDemo() {
    ChaiDCKE22Theme {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = ChaiWhite)
                .padding(horizontal = 13.dp, vertical = 5.dp)
        ) {
            BreathingSpace26()
            CPageTitle("Chai Demo")
            SeparatorSpace()
            CSubtitleRed("A catalog of the chai design system elements")
            SeparatorSpace()
            CParagraph("Check the code that is with each view")
            BreathingSpace13()
        }
    }
}