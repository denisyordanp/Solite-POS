package com.socialite.solite_pos.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StoreMenuItem(
    menuText: String,
    onClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() }
            .background(
                color = MaterialTheme.colors.surface
            )
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = menuText,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colors.onSurface
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}
