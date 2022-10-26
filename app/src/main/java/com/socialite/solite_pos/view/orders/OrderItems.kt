package com.socialite.solite_pos.view.orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BadgeNumber
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.ModalContent
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun OrderItems(
    orderViewModel: OrderViewModel,
    currentDate: String,
    defaultTabPage: Int,
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit,
    onOrderClicked: (orderNo: String) -> Unit
) {

    val modalContent by remember { mutableStateOf(ModalContent.GENERAL_MENUS) }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalState,
        sheetShape = RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp
        ),
        sheetContent = {
            when (modalContent) {
                ModalContent.GENERAL_MENUS -> GeneralMenusView(
                    orderViewModel = orderViewModel,
                    date = currentDate,
                    onClicked = {
                        if (it == GeneralMenus.ORDERS) {
                            scope.launch {
                                modalState.hide()
                            }
                        } else {
                            onGeneralMenuClicked(it)
                        }
                    }
                )

                else -> {
                    // Do nothing
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                OrderList(
                    viewModel = orderViewModel,
                    currentDate = currentDate,
                    defaultTabPage = defaultTabPage,
                    onOrderClicked = onOrderClicked
                )
                GeneralMenuButtonView(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    onMenuClicked = {
                        scope.launch {
                            modalState.show()
                        }
                    }
                )
            }
        }
    )
}

@Composable
@ExperimentalPagerApi
private fun OrderList(
    viewModel: OrderViewModel,
    currentDate: String,
    defaultTabPage: Int,
    onOrderClicked: (orderNo: String) -> Unit
) {
    Column {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        val menus = OrderMenus.values()

        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            indicator = {
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, it)
                )
            },
            edgePadding = 16.dp
        ) {
            menus.forEachIndexed { i, menu ->
                Tab(
                    selected = pagerState.currentPage == i,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                ) {
                    val badge = viewModel.getOrderBadge(menu, currentDate)
                        .collectAsState(initial = null)

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        badge.value?.let {
                            BadgeNumber(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                badge = it
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            modifier = Modifier,
                            text = stringResource(id = menu.title),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

        HorizontalPager(
            count = menus.size,
            state = pagerState
        ) { page ->
            OrderContent(
                menu = menus[page],
                currentDate = currentDate,
                viewModel = viewModel,
                onOrderClicked = onOrderClicked
            )
        }

        if (defaultTabPage != 0) {
            LaunchedEffect(key1 = true) {
                pagerState.animateScrollToPage(defaultTabPage)
            }
        }
    }
}

@Composable
private fun OrderContent(
    menu: OrderMenus,
    currentDate: String,
    viewModel: OrderViewModel,
    onOrderClicked: (orderNo: String) -> Unit
) {

    val orders =
        viewModel.getOrderList(menu.status, currentDate).collectAsState(initial = emptyList())

    if (orders.value.isEmpty()) {
        EmptyOrders(menu = menu)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                items(orders.value) {
                    OrderItem(
                        orderData = it,
                        viewModel = viewModel,
                        onOrderClicked = onOrderClicked
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyOrders(
    menu: OrderMenus
) {
    val (id, text) = when (menu) {
        OrderMenus.CURRENT_ORDER -> Pair(
            R.drawable.ic_on_process,
            R.string.yeay_all_order_are_done
        )
        OrderMenus.NOT_PAY_YET -> Pair(
            R.drawable.ic_get_payment,
            R.string.all_orders_are_paid_already
        )
        OrderMenus.CANCELED -> Pair(
            R.drawable.ic_happy_face,
            R.string.yeay_there_no_cancel_order
        )
        OrderMenus.DONE -> Pair(
            R.drawable.ic_cancel_order,
            R.string.no_done_order_yet_spirit_find_some_customer_today
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                painter = painterResource(id = id),
                contentScale = ContentScale.FillWidth,
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
}

@Composable
private fun OrderItem(
    orderData: OrderData,
    viewModel: OrderViewModel,
    onOrderClicked: (orderNo: String) -> Unit
) {

    var orderProducts by remember {
        mutableStateOf(
            OrderWithProduct(
                order = orderData
            )
        )
    }

    LaunchedEffect(key1 = orderData) {
        viewModel.getProductOrder(orderData.order.orderNo)
            .collectLatest {
                orderProducts = orderProducts.copy(
                    products = it
                )
            }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .clickable {
                onOrderClicked(orderProducts.order.order.orderNo)
            }
            .background(
                color = Color.White
            )
            .padding(16.dp)
    ) {

        val (name, total, dine, icon) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            text = orderProducts.order.customer.name,
            style = MaterialTheme.typography.body1
        )
        Text(
            modifier = Modifier
                .constrainAs(total) {
                    top.linkTo(name.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                },
            text = "Rp. ${orderProducts.grandTotal.thousand()}",
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            modifier = Modifier
                .constrainAs(dine) {
                    linkTo(top = total.top, bottom = total.bottom)
                    start.linkTo(total.end, margin = 16.dp)
                },
            text = stringResource(
                id = if (orderProducts.order.order.isTakeAway) R.string.take_away else R.string.dine_in
            ),
            style = MaterialTheme.typography.overline.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Icon(
            modifier = Modifier
                .constrainAs(icon) {
                    end.linkTo(parent.end)
                    linkTo(top = parent.top, bottom = parent.bottom)
                },
            painter = painterResource(id = R.drawable.ic_dimsum_50dp),
            tint = MaterialTheme.colors.background,
            contentDescription = null
        )
    }
}