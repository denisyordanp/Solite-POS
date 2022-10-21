package com.socialite.solite_pos.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun MenuItemView(
    @StringRes title: Int,
    @DrawableRes icon: Int? = null,
    badgeNumber: Int? = null,
    onClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClicked() }
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Image(painter = painterResource(id = icon), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
            badgeNumber?.let {
                Spacer(modifier = Modifier.width(8.dp))
                BadgeNumber(badge = it)
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
