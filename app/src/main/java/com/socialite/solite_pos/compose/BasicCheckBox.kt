package com.socialite.solite_pos.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicCheckBox(
    titleText: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.onSurface
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = titleText,
            style = MaterialTheme.typography.body2
        )
    }
}
