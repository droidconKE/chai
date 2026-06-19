package com.droidconke.chaidemo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.droidconke.chai.ChaiTheme
import com.droidconke.chai.components.CPrimaryButton
import com.droidconke.chai.components.COutlinedPrimaryButton
import com.droidconke.chai.components.ChaiTextBody
import com.droidconke.chai.utils.Space15
import com.droidconke.chai.utils.Space30
import com.droidconke.chai.utils.Spacer30

@Preview(showBackground = true)
@Composable
fun ChaiButtonsScreen() {
    ChaiTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = ChaiTheme.colors.background)
                .padding(horizontal = Space15, vertical = Space30)
        ) {
            ChaiTextBody(text = "chai Buttons Demo Screens")
            Spacer30()
            CPrimaryButton(
                onClick = { },
                isEnabled = true,
                title = "Primary Button",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer30()
            CPrimaryButton(
                onClick = { },
                isEnabled = false,
                title = "Primary Button Disabled",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer30()
            COutlinedPrimaryButton(
                onClick = { },
                title = "Outlined Button",
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.AutoMirrored.Filled.Send
            )
        }
    }
}