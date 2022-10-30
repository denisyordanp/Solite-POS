package com.socialite.solite_pos.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.BasicEmptyList(
    @DrawableRes imageId: Int,
    @StringRes text: Int
) {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp),
            painter = painterResource(id = imageId),
            contentScale = ContentScale.Inside,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(text),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
}
