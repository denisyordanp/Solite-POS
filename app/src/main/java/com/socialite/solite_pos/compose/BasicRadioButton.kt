package com.socialite.solite_pos.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun BasicRadioButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    text: String,
    onClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onClicked()
            }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClicked
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = text,
            style = MaterialTheme.typography.body2
        )
    }
}
