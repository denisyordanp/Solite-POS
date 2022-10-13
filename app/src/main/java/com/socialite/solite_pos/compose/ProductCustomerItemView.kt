package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.socialite.solite_pos.R
import com.socialite.solite_pos.utils.config.toIDR

@Composable
fun ProductCustomerItemView(
    titleText: String,
    subTitleText: String,
    priceText: Long,
    imageUrl: String?,
    onAddItemClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White
            )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            val (image, title, subTitle, price, button) = createRefs()
            val imageSource = if (imageUrl.isNullOrEmpty()) {
                painterResource(id = R.drawable.ic_dimsum_50dp)
            } else {
                rememberAsyncImagePainter(model = imageUrl)
            }
            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
                painter = imageSource,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        linkTo(
                            start = image.end,
                            startMargin = 16.dp,
                            end = price.start,
                            endMargin = 16.dp,
                            bias = 0f
                        )
                        top.linkTo(parent.top)
                    },
                text = titleText,
                style = MaterialTheme.typography.h6
            )
            Text(
                modifier = Modifier
                    .constrainAs(subTitle) {
                        linkTo(
                            start = image.end,
                            startMargin = 16.dp,
                            end = price.start,
                            endMargin = 16.dp,
                            bias = 0f
                        )
                        linkTo(top = title.bottom, bottom = parent.bottom)
                    },
                text = subTitleText
            )
            Text(
                modifier = Modifier
                    .constrainAs(price) {
                        end.linkTo(button.start, margin = 8.dp)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
                text = priceText.toIDR(),
                style = MaterialTheme.typography.h6
            )
            IconButton(
                modifier = Modifier
                    .constrainAs(button) {
                        end.linkTo(parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
                onClick = onAddItemClick
            ) {
                Image(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.ic_add_circle),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
                    contentDescription = null
                )
            }
        }
    }
}
