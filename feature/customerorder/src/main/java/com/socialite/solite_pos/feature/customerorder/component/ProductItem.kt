package com.socialite.solite_pos.feature.customerorder.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.common.ui.component.ImageLoader
import com.socialite.core.extensions.toIDR
import com.socialite.core.ui.extension.size16Normal
import com.socialite.core.ui.extension.size14Bold
import com.socialite.core.ui.extension.round12
import com.socialite.core.ui.extension.size10Normal
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.spanStyles
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Product

@Composable
fun ProductItem(
    product: Product,
    categoryItem: Category? = null
) {
    val paddings = MaterialTheme.paddings

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        val (image, card, name, desc, category, price) = createRefs()

        Surface(
            modifier = Modifier
                .constrainAs(card) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        startMargin = paddings.extraLarge
                    )
                    linkTo(top = parent.top, bottom = parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
            color = MaterialTheme.colors.surface,
            shape = MaterialTheme.shapes.round12,
            content = {}
        )

        ImageLoader(
            modifier = Modifier
                .width(100.dp)
                .constrainAs(image) {
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom,
                        topMargin = paddings.smallMedium,
                        bottomMargin = paddings.smallMedium
                    )
                    start.linkTo(parent.start)
                    height = Dimension.fillToConstraints
                }, source = product.image
        )

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    linkTo(
                        start = image.end,
                        end = parent.end,
                        startMargin = paddings.medium,
                        endMargin = paddings.medium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top, margin = paddings.medium)
                },
            text = product.name,
            style = MaterialTheme.typography.size14Bold,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier
                .constrainAs(desc) {
                    linkTo(
                        start = image.end,
                        end = parent.end,
                        startMargin = paddings.medium,
                        endMargin = paddings.medium
                    )
                    width = Dimension.fillToConstraints
                    top.linkTo(name.bottom, margin = paddings.small)
                },
            text = product.name,
            style = MaterialTheme.typography.size10Normal,
            textAlign = TextAlign.Start
        )

        Text(
            modifier = Modifier
                .constrainAs(category) {
                    linkTo(
                        start = image.end,
                        end = price.start,
                        startMargin = paddings.medium,
                        endMargin = paddings.small
                    )
                    bottom.linkTo(parent.bottom, margin = paddings.medium)
                    width = Dimension.fillToConstraints
                },
            text = categoryItem?.name.orEmpty(),
            style = MaterialTheme.typography.size14Bold,
            textAlign = TextAlign.Start
        )

        val priceText = buildAnnotatedString {
            append(stringResource(R.string.idr_title))
            withStyle(MaterialTheme.spanStyles.bold) {
                append(" ${product.price.toIDR()}")
            }
        }
        Text(
            modifier = Modifier
                .constrainAs(price) {
                    end.linkTo(parent.end, margin = paddings.medium)
                    bottom.linkTo(parent.bottom, margin = paddings.medium)
                },
            text = priceText,
            style = MaterialTheme.typography.size16Normal,
            textAlign = TextAlign.Start
        )
    }
}