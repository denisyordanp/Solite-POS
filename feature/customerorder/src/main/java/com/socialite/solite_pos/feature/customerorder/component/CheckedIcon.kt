package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round8
import com.socialite.core.ui.theme.SolitePOSTheme
import com.socialite.feature.customerorder.R

@Composable
fun CheckedIcon(
    isChecked: Boolean = true
) {
    val bgColor = if (isChecked) MaterialTheme.colors.primary else MaterialTheme.colors.background

    Surface(
        modifier = Modifier
            .size(16.dp),
        color = bgColor,
        shape = MaterialTheme.shapes.round8
    ) {
        if (isChecked) {
            Icon(
                modifier = Modifier
                    .padding(MaterialTheme.paddings.extraSmall),
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
        } else {
            Spacer(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SolitePOSTheme {
        CheckedIcon(true)
    }
}