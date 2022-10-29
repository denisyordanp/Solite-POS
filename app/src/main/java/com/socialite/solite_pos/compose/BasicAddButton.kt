package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R

@Composable
fun BasicAddButton(
    modifier: Modifier = Modifier,
    onAddClicked: () -> Unit
) {
    Surface(
        modifier = modifier
            .size(50.dp)
            .clickable { onAddClicked() },
        elevation = 4.dp,
        shape = CircleShape,
        color = MaterialTheme.colors.primary
    ) {
        Image(
            modifier = Modifier
                .padding(10.dp),
            painter = painterResource(id = R.drawable.ic_add),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentDescription = null
        )
    }
}
