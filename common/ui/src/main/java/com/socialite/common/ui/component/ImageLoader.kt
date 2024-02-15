package com.socialite.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.socialite.common.ui.R
import com.socialite.core.ui.extension.round8

@Composable
fun ImageLoader(
    modifier: Modifier,
    source: Any?
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.onSurface,
        shape = MaterialTheme.shapes.round8
    ) {
        Box {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_image_placeholder),
                contentDescription = null
            )
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = source,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}