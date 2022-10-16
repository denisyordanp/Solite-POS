package com.socialite.solite_pos.view.main.opening.orders

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.main.opening.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.main.opening.store.StoreActivity
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.OrderViewModel

class OrdersActivity : AppCompatActivity() {

    private lateinit var orderViewModel: OrderViewModel

    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)

        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = OrderDetailDestinations.ORDERS
                ) {
                    composable(
                        route = OrderDetailDestinations.ORDERS
                    ) {
                        OrderItems(
                            viewModel = orderViewModel,
                            currentDate = DateUtils.currentDate,
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
}
