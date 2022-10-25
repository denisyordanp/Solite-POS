package com.socialite.solite_pos.view.orders

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class OrdersActivity : AppCompatActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainViewModel: MainViewModel

    private var printBill: PrintBill? = null

    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    @ExperimentalCoroutinesApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)

        printBill = PrintBill(this)

        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()
                var defaultTabPage by remember {
                    mutableStateOf(0)
                }

                NavHost(
                    navController = navController,
                    startDestination = OrderDetailDestinations.ORDERS
                ) {
                    composable(
                        route = OrderDetailDestinations.ORDERS
                    ) {
                        OrderItems(
                            orderViewModel = orderViewModel,
                            currentDate = DateUtils.currentDate,
                            defaultTabPage = defaultTabPage,
                            onGeneralMenuClicked = {
                                when (it) {
                                    GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                                    GeneralMenus.STORE -> goToStoreActivity()
                                    GeneralMenus.SETTING -> TODO()
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            },
                            onOrderClicked = {
                                navController.navigate(OrderDetailDestinations.orderDetail(it))
                            }
                        )
                    }

                    val orderNoArgument = navArgument(name = OrderDetailDestinations.ORDER_NO) {
                        type = NavType.StringType
                    }
                    composable(
                        route = OrderDetailDestinations.ORDER_DETAIL,
                        arguments = listOf(orderNoArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.ORDER_NO)?.let { orderNo ->
                            OrderDetail(
                                orderNo = orderNo,
                                orderViewModel = orderViewModel,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onButtonClicked = {buttonType, orderWithProduct ->
                                    when (buttonType) {
                                        OrderButtonType.PRINT -> {
                                            lifecycleScope.launch {
                                                val store = mainViewModel.getStore()
                                                if (store != null && orderWithProduct != null) {
                                                    printBill?.doPrintBill(
                                                        order = orderWithProduct,
                                                        store = store,
                                                        callback = {}
                                                    )
                                                }
                                            }
                                        }
                                        OrderButtonType.QUEUE -> {
                                            lifecycleScope.launch {
                                                if (orderWithProduct != null) {
                                                    printBill?.doPrintQueue(
                                                        order = orderWithProduct,
                                                        callback = {}
                                                    )
                                                }
                                            }
                                        }
                                        OrderButtonType.PAYMENT -> {
                                            navController.navigate(
                                                OrderDetailDestinations.orderPayment(
                                                    orderNo
                                                )
                                            )
                                        }
                                        OrderButtonType.DONE -> {
                                            defaultTabPage = OrderMenus.NOT_PAY_YET.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        OrderButtonType.CANCEL -> {
                                            defaultTabPage = OrderMenus.CANCELED.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        OrderButtonType.PUT_BACK -> {
                                            defaultTabPage = OrderMenus.CURRENT_ORDER.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                },
                            )
                        }
                    }
                    composable(
                        route = OrderDetailDestinations.ORDER_PAYMENT,
                        arguments = listOf(orderNoArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.ORDER_NO)?.let { orderNo ->
                            OrderPaymentView(
                                orderNo = orderNo,
                                orderViewModel = orderViewModel,
                                mainViewModel = mainViewModel,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onPayClicked = { order, payment, pay ->
                                    lifecycleScope.launch {
                                        orderViewModel.payOrder(
                                            order = order,
                                            payment = payment,
                                            pay = pay
                                        )
                                        defaultTabPage = OrderMenus.DONE.status
                                        navController.navigate(OrderDetailDestinations.ORDERS) {
                                            popUpTo(OrderDetailDestinations.ORDERS) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    @ExperimentalMaterialApi
    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        printBill?.onDestroy()
        printBill = null
        super.onDestroy()
    }
}
