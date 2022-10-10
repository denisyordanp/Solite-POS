package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun BucketView(
    customerName: String = "Denis Yordan",
    orderDate: String = "04 Oktober 2022, 15:30",
    onClickOrder: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = customerName,
            style = MaterialTheme.typography.h6
        )
        Text(
            text = orderDate,
            style = MaterialTheme.typography.overline
        )
        Spacer(modifier = Modifier.height(24.dp))
        BucketItem()
        BucketItem()
        TotalBucket()
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButtonView(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            buttonText = stringResource(id = R.string.order_now),
            onClick = onClickOrder
        )
    }
}

@Composable
private fun BucketItem() {
    val textStyle = MaterialTheme.typography.caption
    Column {
        Row {
            Text(
                text = "1x",
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
                    text = "Dimsum yang sangat pedas dan gurih asin manis juga",
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
                    text = "Level pedas 1, Kukus, Mayonaise, Manis, Asam, Pahit",
                    style = MaterialTheme.typography.overline
                )
                Text(
                    modifier = Modifier
                        .constrainAs(price) {
                            end.linkTo(delete.start)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                    text = "Rp. 5000",
                    style = MaterialTheme.typography.body2
                )
                TextButton(
                    modifier = Modifier
                        .constrainAs(delete) {
                            end.linkTo(parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                    onClick = { /*TODO*/ }
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
private fun ColumnScope.TotalBucket() {
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
            text = "Rp.10.000",
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(60.dp))
    }
}
