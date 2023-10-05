package com.socialite.solite_pos.feature.customerorder.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.common.ui.component.MainBackground
import com.socialite.core.ui.theme.SolitePOSTheme

@Composable
fun MainOrderScreen() {
    MainBackground {
        Scaffold { padding ->
            Spacer(
                modifier = Modifier
                    .padding(padding)
                    .height(200.dp)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SolitePOSTheme {
        MainOrderScreen()
    }
}