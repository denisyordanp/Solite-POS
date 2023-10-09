package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.common.ui.component.ImageLoader
import com.socialite.core.ui.extension.body2Bold
import com.socialite.core.ui.extension.item
import com.socialite.core.ui.extension.overLineNormal
import com.socialite.core.ui.extension.paddings
import com.socialite.schema.ui.main.Product

@Composable
fun FavoriteMenuItem(product: Product) {
    val paddings = MaterialTheme.paddings

    ConstraintLayout(
        modifier = Modifier
            .size(
                width = 150.dp,
                height = 170.dp
            )
            .padding(end = paddings.smallMedium)
    ) {
        val (image, card, name, desc) = createRefs()

        Surface(
            modifier = Modifier
                .constrainAs(card) {
                    linkTo(start = parent.start, end = parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom, topMargin = paddings.medium)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.item,
            content = {}
        )

        ImageLoader(
            modifier = Modifier
                .height(80.dp)
                .constrainAs(image) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                }, source = product.image
        )

        Text(
            modifier = Modifier
                .height(35.dp)
                .constrainAs(name) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(image.bottom, margin = paddings.smallMedium)
                },
            text = product.name,
            style = MaterialTheme.typography.body2Bold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .constrainAs(desc) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.smallMedium,
                        endMargin = paddings.smallMedium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(name.bottom, margin = paddings.extraSmall)
                },
            text = product.desc,
            style = MaterialTheme.typography.overLineNormal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}