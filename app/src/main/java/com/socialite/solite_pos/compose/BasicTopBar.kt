package com.socialite.solite_pos.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.socialite.solite_pos.R

@Composable
fun BasicTopBar(
    titleText: String,
    onBackClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 8.dp,
        color = MaterialTheme.colors.primary
    ) {
        Row {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { onBackClicked() }
                    .padding(start = 8.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                text = titleText,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}
