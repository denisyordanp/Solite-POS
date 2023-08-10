package com.socialite.solite_pos.view.orders.order_items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BadgeNumber
import com.socialite.solite_pos.compose.BasicEmptyList
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.GeneralMenuButtonView
import com.socialite.solite_pos.compose.GeneralMenusView
import com.socialite.solite_pos.compose.SpaceForFloatingButton
import com.socialite.solite_pos.data.source.local.entity.helper.GeneralMenuBadge
import com.socialite.solite_pos.data.source.local.entity.helper.OrderMenuWithOrders
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.ModalContent
import com.socialite.solite_pos.view.ui.OrderMenus
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
fun OrderItemsScreen(
    currentViewModel: OrderItemsViewModel = hiltViewModel(),
    parameters: ReportParameter,
    defaultTabPage: Int,
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit,
    onOrderClicked: (orderId: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val orders = currentViewModel.getOrders(parameters).collectAsState(initial = emptyList()).value

    if (parameters.isTodayOnly()) {
        val badges =
            currentViewModel.getBadges(parameters.start).collectAsState(initial = emptyList())
        TodayOnlyOrdersContent(
            badges = badges.value,
            ordersMenu = orders,
            defaultTabPage = defaultTabPage,
            onGeneralMenuClicked = onGeneralMenuClicked,
            onOrderClicked = onOrderClicked
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Scaffold(
                topBar = {
                    BasicTopBar(
                        titleText = parameters.toTitle(),
                        onBackClicked = onBackClicked
                    )
                },
                content = { padding ->
                    OrderList(
                        modifier = Modifier
                            .padding(padding),
                        ordersMenu = orders,
                        defaultTabPage = defaultTabPage,
                        onOrderClicked = onOrderClicked,
                    )
                }
            )
        }
    }
}

@Composable
@ExperimentalPagerApi
@ExperimentalMaterialApi
private fun TodayOnlyOrdersContent(
    badges: List<GeneralMenuBadge>,
    ordersMenu: List<OrderMenuWithOrders>,
    defaultTabPage: Int,
    onGeneralMenuClicked: (menu: GeneralMenus) -> Unit,
    onOrderClicked: (orderId: String) -> Unit,
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
                    badges = badges,
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
                var shouldShowButton by remember { mutableStateOf(true) }

                OrderList(
                    ordersMenu = ordersMenu,
                    defaultTabPage = defaultTabPage,
                    onOrderClicked = onOrderClicked,
                    onScrollProgress = {
                        shouldShowButton = !it
                    }
                )
                AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(24.dp),
                    visible = shouldShowButton,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    GeneralMenuButtonView(
                        onMenuClicked = {
                            scope.launch {
                                modalState.show()
                            }
                        }
                    )
                }
            }
        }
    )
}

@Composable
@ExperimentalPagerApi
private fun OrderList(
    modifier: Modifier = Modifier,
    ordersMenu: List<OrderMenuWithOrders>,
    defaultTabPage: Int,
    onOrderClicked: (orderId: String) -> Unit,
    onScrollProgress: ((Boolean) -> Unit)? = null
) {
    Column {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()

        Surface(
            modifier = modifier,
            elevation = 8.dp,
            color = MaterialTheme.colors.primary
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    if (it.isNotEmpty()) TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, it)
                    )
                },
                edgePadding = 16.dp
            ) {
                ordersMenu.forEachIndexed { i, menu ->
                    Tab(
                        selected = pagerState.currentPage == i,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(i)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            menu.getBadges()?.let {
                                BadgeNumber(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically),
                                    badge = it
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                modifier = Modifier,
                                text = stringResource(id = menu.menu.title),
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
        }

        HorizontalPager(
            count = ordersMenu.size,
            state = pagerState
        ) { page ->
            OrderContent(
                menuOrders = ordersMenu[page],
                onOrderClicked = onOrderClicked,
                onScrollProgress = onScrollProgress
            )
        }

        if (defaultTabPage != 0 && pagerState.pageCount != 0) {
            LaunchedEffect(key1 = true) {
                pagerState.animateScrollToPage(defaultTabPage)
            }
        }
    }
}

@Composable
private fun OrderContent(
    menuOrders: OrderMenuWithOrders,
    onOrderClicked: (orderId: String) -> Unit,
    onScrollProgress: ((Boolean) -> Unit)?
) {
    if (menuOrders.orders.isEmpty()) {
        EmptyOrders(menu = menuOrders.menu)
    } else {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxSize()
        ) {
            val listState = rememberLazyListState()
            onScrollProgress?.invoke(listState.isScrollInProgress)

            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter),
                state = listState
            ) {
                items(menuOrders.orders) {
                    OrderItem(
                        orderProducts = it,
                        onOrderClicked = onOrderClicked
                    )
                }

                item { SpaceForFloatingButton() }
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
            .background(color = MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        BasicEmptyList(imageId = id, text = text)
    }
}

@Composable
private fun OrderItem(
    orderProducts: OrderWithProduct,
    onOrderClicked: (orderId: String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .clickable {
                onOrderClicked(orderProducts.orderData.order.id)
            }
            .background(
                color = MaterialTheme.colors.surface
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
            text = orderProducts.orderData.customer.name,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
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
            ),
            color = MaterialTheme.colors.onSurface
        )
        Text(
            modifier = Modifier
                .constrainAs(dine) {
                    linkTo(top = total.top, bottom = total.bottom)
                    start.linkTo(total.end, margin = 16.dp)
                },
            text = stringResource(
                id = if (orderProducts.orderData.order.isTakeAway) R.string.take_away else R.string.dine_in
            ),
            style = MaterialTheme.typography.overline.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colors.onSurface
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
