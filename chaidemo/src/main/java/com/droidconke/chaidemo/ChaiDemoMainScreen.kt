package com.droidconke.chaidemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.droidconke.chai.ChaiTheme
import com.droidconke.chai.atoms.ChaiWhite
import com.droidconke.chai.components.CPageTitle
import com.droidconke.chai.components.CParagraph
import com.droidconke.chai.utils.Space15
import com.droidconke.chai.utils.Space30
import com.droidconke.chai.utils.spacer30

@Preview(showBackground = true)
@Composable
fun ChaiDemoMainScreen() {
    ChaiTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = ChaiWhite)
                .padding(horizontal = Space15, vertical = Space30)
        ) {
            CPageTitle("chai demo main screen")
            spacer30()
            CParagraph(dParagraph = "This is the main screen of the chai demo app.")
        }
    }
}