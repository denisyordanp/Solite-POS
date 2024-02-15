package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.socialite.solite_pos.R
import com.socialite.common.utility.helper.DateUtils
import com.socialite.schema.ui.helper.ProductOrderDetail
import com.socialite.core.extensions.thousand
import com.socialite.core.extensions.timeMilliSecondToDateFormat

@Composable
fun BucketView(
    bucketOrder: com.socialite.schema.ui.helper.BucketOrder,
    isEditOrder: Boolean,
    onClickOrder: () -> Unit,
    onClearBucket: () -> Unit,
    onRemoveProduct: (detail: ProductOrderDetail) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        item {
            val time =
                bucketOrder.time?.timeMilliSecondToDateFormat(DateUtils.DATE_WITH_DAY_AND_TIME_FORMAT)
                    ?.run {
                        Pair(
                            first = this.substring(0..19),
                            second = this.substring(21 until this.length),
                        )
                    }
            Text(
                text = time?.first ?: "",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = time?.second ?: "",
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        bucketOrder.products?.let {
            items(it) { detail ->
                BucketItem(
                    detail = detail,
                    onRemoveItem = {
                        if (it.size == 1) {
                            onClearBucket()
                        }
                        onRemoveProduct(detail)
                    }
                )
            }
        }

        item {
            TotalBucket(
                total = bucketOrder.getTotal().thousand()
            )
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                buttonText = stringResource(
                    id = if (isEditOrder) R.string.edit_order else R.string.order_now
                ),
                onClick = onClickOrder
            )
        }
    }
}

@Composable
private fun BucketItem(
    detail: ProductOrderDetail,
    onRemoveItem: () -> Unit
) {
    val textStyle = MaterialTheme.typography.caption
    Column {
        Row {
            Text(
                text = "${detail.amount}x",
                style = textStyle
            )
            Spacer(modifier = Modifier.width(8.dp))
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (name, variants, price, delete) = createRefs()
                Text(
                    modifier = Modifier
                        .constrainAs(name) {
                            linkTo(start = parent.start, end = price.start, endMargin = 16.dp)
                            top.linkTo(parent.top)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    text = detail.product?.name ?: "",
                    style = textStyle.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    modifier = Modifier
                        .constrainAs(variants) {
                            top.linkTo(name.bottom, margin = 4.dp)
                            linkTo(start = parent.start, end = price.start, endMargin = 16.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    text = detail.generateVariantsString(),
                    style = MaterialTheme.typography.overline
                )
                Text(
                    modifier = Modifier
                        .constrainAs(price) {
                            end.linkTo(delete.start)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                    text = "Rp. ${detail.totalPrice().thousand()}",
                    style = MaterialTheme.typography.body2
                )
                TextButton(
                    modifier = Modifier
                        .constrainAs(delete) {
                            end.linkTo(parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                    onClick = onRemoveItem
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(
                    color = MaterialTheme.colors.onPrimary,
                )
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun TotalBucket(
    total: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.End)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp),
                text = "Total :",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "Rp. $total",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.width(60.dp))
        }
    }
}
