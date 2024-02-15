package com.socialite.solite_pos.feature.customerorder.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.common.ui.component.PrimaryButton
import com.socialite.common.ui.component.SoliteContent
import com.socialite.common.utility.helper.DateUtils
import com.socialite.core.extensions.timeMilliSecondToDateFormat
import com.socialite.core.extensions.toIDR
import com.socialite.core.ui.extension.paddings
import com.socialite.core.ui.extension.round12
import com.socialite.core.ui.extension.size12Bold
import com.socialite.core.ui.extension.size12Normal
import com.socialite.core.ui.extension.size14SemiBold
import com.socialite.core.ui.extension.size16Normal
import com.socialite.core.ui.extension.spanStyles
import com.socialite.feature.customerorder.R
import com.socialite.schema.ui.dummy.DummySchema
import com.socialite.schema.ui.helper.BucketOrder
import com.socialite.schema.ui.helper.ProductOrderDetail

@Composable
fun BucketOrderBottomSheet(
    modifier: Modifier = Modifier,
    bucketOrder: BucketOrder,
    onRemoveItem: (product: ProductOrderDetail) -> Unit,
    onOrderNowClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = MaterialTheme.paddings.medium)
            .padding(bottom = MaterialTheme.paddings.medium)
    ) {
        val time =
            bucketOrder.time?.timeMilliSecondToDateFormat(DateUtils.DATE_WITH_DAY_AND_TIME_FORMAT)
                .run {
                    Pair(
                        first = this?.substring(0..19).orEmpty(),
                        second = this?.substring(21 until this.length).orEmpty(),
                    )
                }

        DateTime(
            date = time.first,
            time = time.second
        )

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.medium))

        LazyColumn {
            bucketOrder.products?.let { products ->
                items(products) {
                    BucketItem(
                        order = it,
                        onRemoveClick = {
                            onRemoveItem(it)
                        }
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))
                }
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.smallMedium))

        val totalText = buildAnnotatedString {
            append("Total")
            withStyle(MaterialTheme.spanStyles.bold) {
                append("  ${bucketOrder.getTotal().toIDR()}")
            }
        }
        Text(
            modifier = Modifier
                .padding(end = 65.dp)
                .align(Alignment.End),
            text = totalText,
            style = MaterialTheme.typography.size16Normal
        )

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.large))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.order_now_button),
            isEnabled = !bucketOrder.products.isNullOrEmpty(),
            onClick = onOrderNowClick
        )
    }
}

@Composable
private fun BucketItem(
    order: ProductOrderDetail,
    onRemoveClick: () -> Unit
) {
    val paddings = MaterialTheme.paddings

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.round12)
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = paddings.medium, vertical = paddings.smallMedium)
    ) {
        val (amount, name, variants, total, delete) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(amount) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            text = "${order.amount}x",
            style = MaterialTheme.typography.size12Normal
        )
        Text(
            modifier = Modifier
                .constrainAs(name) {
                    linkTo(
                        start = amount.end,
                        end = total.start,
                        startMargin = paddings.small,
                        endMargin = paddings.small
                    )
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                },
            text = order.product?.name.orEmpty(),
            style = MaterialTheme.typography.size12Bold
        )
        Text(
            modifier = Modifier
                .constrainAs(variants) {
                    start.linkTo(name.start)
                    linkTo(
                        start = name.start,
                        end = name.end,
                    )
                    top.linkTo(name.bottom, margin = paddings.extraSmall)
                    width = Dimension.fillToConstraints
                },
            text = order.generateVariantsString(),
            style = MaterialTheme.typography.size12Normal
        )
        Text(
            modifier = Modifier
                .constrainAs(total) {
                    end.linkTo(delete.start)
                    linkTo(top = parent.top, bottom = parent.bottom)
                },
            text = order.totalPrice().toIDR(),
            style = MaterialTheme.typography.size14SemiBold
        )
        IconButton(
            modifier = Modifier
                .constrainAs(delete) {
                    end.linkTo(parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom)
                },
            onClick = onRemoveClick,
            content = {
                Icon(
                    modifier = Modifier.padding(paddings.extraSmall),
                    painter = painterResource(id = R.drawable.ic_trash),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        )
    }
}

@Composable
private fun DateTime(
    date: String,
    time: String
) {
    Column {
        Text(
            text = date,
            style = MaterialTheme.typography.size14SemiBold
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.extraSmall))
        Text(
            text = time,
            style = MaterialTheme.typography.size12Normal
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SoliteContent {
        BucketOrderBottomSheet(
            modifier = it,
            bucketOrder = DummySchema.bucketOrder,
            onRemoveItem = {},
            onOrderNowClick = {}
        )
    }
}