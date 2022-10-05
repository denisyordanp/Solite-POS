package com.socialite.solite_pos.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.socialite.solite_pos.R

@Composable
fun GeneralMenus(
    onClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        MenuItem(
            title = R.string.new_order,
            icon = R.drawable.ic_masters,
            onClicked = onClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            title = R.string.orders,
            icon = R.drawable.ic_cook,
            onClicked = onClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            title = R.string.store,
            icon = R.drawable.ic_store,
            onClicked = onClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuItem(
            title = R.string.setting,
            icon = R.drawable.ic_settings,
            onClicked = onClicked
        )
    }
}

@Composable
private fun MenuItem(
    @StringRes title: Int,
    @DrawableRes icon: Int,
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
            Image(painter = painterResource(id = icon), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}
