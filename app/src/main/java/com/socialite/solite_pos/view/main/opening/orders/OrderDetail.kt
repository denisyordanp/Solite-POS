package com.socialite.solite_pos.view.main.opening.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
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
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.utils.config.rupiahToK
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.main.opening.ui.OrderMenus
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.flow.first

@Composable
fun OrderDetail(
    orderNo: String,
    orderViewModel: OrderViewModel,
    onBackClicked: () -> Unit,
    onDoneClicked: () -> Unit,
    onPayClicked: () -> Unit,
) {

    var orderWithProducts by remember {
        mutableStateOf<OrderWithProduct?>(null)
    }

    LaunchedEffect(key1 = orderNo) {
        orderViewModel.getOrderDetail(orderNo)?.let {
            val products = orderViewModel.getProductOrder(orderNo).first()
            orderWithProducts = OrderWithProduct(
                order = it,
                products = products
            )
        }
    }

    var alertDoneState by remember { mutableStateOf(false) }
    var alertCancelState by remember { mutableStateOf(false) }
    var alertPaymentState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = orderWithProducts?.order?.customer?.name ?: "",
                onBackClicked = onBackClicked
            )
        },
        content = { padding ->
            orderWithProducts?.let {
                Details(
                    modifier = Modifier
                        .padding(padding),
                    orderWithProduct = it,
                    onMenuClicked = { buttonType ->
                        when (buttonType) {
                            OrderButtonType.PRINT -> TODO()
                            OrderButtonType.QUEUE -> TODO()
                            OrderButtonType.PAYMENT -> {
                                alertPaymentState = true
                            }
                            OrderButtonType.DONE -> {
                                alertDoneState = true
                            }
                            OrderButtonType.CANCEL -> {
                                alertCancelState = true
                            }
                        }
                    }
                )
            }
        }
    )

    if (alertPaymentState) {
        BasicAlertDialog(
            titleText = stringResource(R.string.pay_the_order),
            descText = stringResource(R.string.are_you_sure_will_pay_this_order),
            positiveAction = {
                onPayClicked()
                alertPaymentState = false
            },
            positiveText = stringResource(R.string.yes),
            negativeAction = {
                alertPaymentState = false
            },
            negativeText = stringResource(R.string.no)
        )
    }

    if (alertCancelState) {
        BasicAlertDialog(
            titleText = stringResource(R.string.cancel_the_order),
            descText = stringResource(R.string.are_you_sure_will_cancel_this_order),
            positiveAction = {
                orderWithProducts?.order?.order?.let {
                    orderViewModel.cancelOrder(it)
                }
                alertCancelState = false
            },
            positiveText = stringResource(R.string.yes),
            negativeAction = {
                alertCancelState = false
            },
            negativeText = stringResource(R.string.no)
        )
    }

    if (alertDoneState) {
        BasicAlertDialog(
            titleText = stringResource(R.string.done_the_order),
            descText = stringResource(R.string.are_you_sure_will_done_this_order),
            positiveAction = {
                orderWithProducts?.order?.order?.let {
                    orderViewModel.doneOrder(it)
                }
                alertDoneState = false
                onDoneClicked()
            },
            positiveText = stringResource(R.string.yes),
            negativeAction = {
                alertDoneState = false
            },
            negativeText = stringResource(R.string.no)
        )
    }
}

@Composable
private fun Details(
    modifier: Modifier = Modifier,
    orderWithProduct: OrderWithProduct,
    onMenuClicked: (OrderButtonType) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            item {
                OrderHeader(
                    order = orderWithProduct.order.order
                )
            }
            itemsIndexed(orderWithProduct.products) { i, product ->
                ProductOrder(
                    number = (i + 1).toString(),
                    productOrderDetail = product
                )
            }
            item {
                OrderFooter(orderWithProduct = orderWithProduct)
            }
        }
        orderWithProduct.order.order.statusToOrderMenu()?.let {
            ButtonBottomBar(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                orderStatus = it,
                onMenuClicked = onMenuClicked
            )
        }
    }
}

@Composable
private fun OrderHeader(
    order: Order
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White
            )
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = order.timeString,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = stringResource(
                    id = if (order.isTakeAway) R.string.take_away else R.string.dine_in
                ),
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = order.getQueueNumber(),
            style = MaterialTheme.typography.h6
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun ProductOrder(
    number: String,
    productOrderDetail: ProductOrderDetail
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White
            )
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "$number.",
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = "x${productOrderDetail.amount}",
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            Text(
                text = productOrderDetail.product?.name ?: "",
                style = MaterialTheme.typography.body2
            )
            Text(
                text = productOrderDetail.generateVariantsString(),
                style = MaterialTheme.typography.overline
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = productOrderDetail.product?.sellPrice?.thousand()?.rupiahToK() ?: "",
            style = MaterialTheme.typography.body2
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            text = productOrderDetail.totalPrice().thousand().rupiahToK(),
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun OrderFooter(
    orderWithProduct: OrderWithProduct
) {
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f),
            text = "${orderWithProduct.totalItem} items",
            style = MaterialTheme.typography.body2
        )
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Rp. ${orderWithProduct.grandTotal.thousand()}",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            orderWithProduct.order.payment?.let {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun ButtonBottomBar(
    modifier: Modifier = Modifier,
    orderStatus: OrderMenus,
    onMenuClicked: (OrderButtonType) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        )
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                when (orderStatus) {
                    OrderMenus.CURRENT_ORDER -> OrderMenuButton(onMenuClicked = onMenuClicked)
                    OrderMenus.NOT_PAY_YET -> NeedPayMenuButton(onMenuClicked = onMenuClicked)
                    OrderMenus.DONE -> DoneMenuButton(onMenuClicked = onMenuClicked)
                    OrderMenus.CANCELED -> OrderMenuButton(onMenuClicked = onMenuClicked)
                }
            }
        }
    }
}

@Composable
private fun RowScope.OrderMenuButton(
    onMenuClicked: (OrderButtonType) -> Unit
) {
    OrderDetailButton(
        buttonType = OrderButtonType.QUEUE,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.PRINT,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.DONE,
        isMain = true,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.PAYMENT,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.CANCEL,
        onMenuClicked = onMenuClicked
    )
}

@Composable
private fun RowScope.NeedPayMenuButton(
    onMenuClicked: (OrderButtonType) -> Unit
) {
    OrderDetailButton(
        buttonType = OrderButtonType.PRINT,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.PAYMENT,
        isMain = true,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.CANCEL,
        onMenuClicked = onMenuClicked
    )
}

@Composable
private fun RowScope.DoneMenuButton(
    onMenuClicked: (OrderButtonType) -> Unit
) {
    OrderDetailButton(
        buttonType = OrderButtonType.QUEUE,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.PRINT,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.DONE,
        isMain = true,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.PAYMENT,
        onMenuClicked = onMenuClicked
    )
    Spacer(modifier = Modifier.width(16.dp))
    OrderDetailButton(
        buttonType = OrderButtonType.CANCEL,
        onMenuClicked = onMenuClicked
    )
}

@Composable
private fun RowScope.OrderDetailButton(
    buttonType: OrderButtonType,
    isMain: Boolean = false,
    onMenuClicked: (OrderButtonType) -> Unit
) {
    Box(
        modifier = Modifier
            .align(Alignment.CenterVertically)
            .background(
                color = if (isMain) MaterialTheme.colors.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable {
                onMenuClicked(buttonType)
            }
            .padding(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(if (isMain) 40.dp else 25.dp),
            painter = painterResource(id = buttonType.icon),
            tint = if (isMain) Color.White else MaterialTheme.colors.primary,
            contentDescription = null
        )
    }
}
