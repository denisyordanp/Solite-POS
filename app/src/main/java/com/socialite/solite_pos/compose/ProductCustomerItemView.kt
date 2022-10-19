package com.socialite.solite_pos.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.utils.config.toIDR
import com.socialite.solite_pos.view.viewModel.ProductViewModel

@Composable
fun ProductCustomerItemView(
    productViewModel: ProductViewModel,
    product: Product,
    currentAmount: Int?,
    onItemClick: (isAdd: Boolean, hasVariant: Boolean) -> Unit
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
            val (title, subTitle, price, buttons) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            end = parent.end,
                            endMargin = 16.dp,
                            bias = 0f
                        )
                        top.linkTo(parent.top)
                    },
                text = product.name,
                style = MaterialTheme.typography.h6
            )
            Text(
                modifier = Modifier
                    .constrainAs(subTitle) {
                        linkTo(
                            start = parent.start,
                            startMargin = 16.dp,
                            end = parent.end,
                            endMargin = 16.dp,
                            bias = 0f
                        )
                        top.linkTo(title.bottom)
                    },
                text = product.desc
            )
            Text(
                modifier = Modifier
                    .constrainAs(price) {
                        start.linkTo(parent.start, margin = 16.dp)
                        linkTo(top = subTitle.bottom, bottom = parent.bottom)
                    },
                text = product.sellPrice.toIDR(),
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Row(
                modifier = Modifier
                    .constrainAs(buttons) {
                        end.linkTo(parent.end)
                        linkTo(top = subTitle.bottom, bottom = parent.bottom)
                    },
            ) {

                var hasVariant by remember {
                    mutableStateOf<Boolean?>(null)
                }

                LaunchedEffect(key1 = true) {
                    hasVariant = productViewModel.isProductHasVariant(product.id)
                }

                hasVariant?.let {
                    if (it) {
                        SelectVariantButton(onItemClick = {
                            onItemClick(true, true)
                        })
                    } else {
                        AmountOption(currentAmount = currentAmount, onItemClick = {isAdd ->
                            onItemClick(isAdd, false)
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.SelectVariantButton(
    onItemClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .clickable {
                onItemClick()
            }
            .align(Alignment.CenterVertically),
        text = stringResource(id = R.string.select_variant),
        style = MaterialTheme.typography.body1
    )
    Spacer(modifier = Modifier.width(8.dp))
    Icon(
        painter = painterResource(id = R.drawable.ic_arrow_right),
        tint = Color.Black,
        contentDescription = null
    )
}

@Composable
private fun RowScope.AmountOption(
    currentAmount: Int?,
    onItemClick: (isAdd: Boolean) -> Unit
) {
    currentAmount?.let {
        IconButton(
            onClick = {
                onItemClick(false)
            }
        ) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_remove_circle),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = currentAmount.toString(),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
    IconButton(
        onClick = {
            onItemClick(true)
        }
    ) {
        Image(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = R.drawable.ic_add_circle),
            contentDescription = null
        )
    }
}
