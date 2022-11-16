package com.socialite.solite_pos.view.order_customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class OrderCustomerActivity : SoliteActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var orderViewModel: OrderViewModel

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel = ProductViewModel.getMainViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)
        orderViewModel = OrderViewModel.getOrderViewModel(this)

        setContent {
            SolitePOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = OrderCustomerDestinations.SELECT_ITEMS
                    ) {
                        composable(
                            OrderCustomerDestinations.SELECT_ITEMS
                        ) {
                            OrderSelectItems(
                                productViewModel = productViewModel,
                                orderViewModel = orderViewModel,
                                onItemClick = { product, isAdd, hasVariant ->
                                    lifecycleScope.launch {
                                        if (hasVariant) {
                                            navController.navigate(
                                                OrderCustomerDestinations.selectVariants(
                                                    product.id
                                                )
                                            )
                                        } else {
                                            if (isAdd) {
                                                orderViewModel.addProductToBucket(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            } else {
                                                orderViewModel.decreaseProduct(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            }
                                        }
                                    }
                                },
                                onClickOrder = {
                                    navController.navigate(OrderCustomerDestinations.SELECT_CUSTOMERS)
                                },
                                onGeneralMenuClicked = {
                                    when (it) {
                                        GeneralMenus.ORDERS -> goToOrdersActivity()
                                        GeneralMenus.STORE -> goToStoreActivity()
                                        GeneralMenus.SETTING -> goToSettingsActivity()
                                        else -> {
                                            // Don nothing
                                        }
                                    }
                                }
                            )
                        }

                        val productIdArgument =
                            navArgument(name = OrderCustomerDestinations.PRODUCT_ID) {
                                type = NavType.LongType
                            }
                        composable(
                            route = OrderCustomerDestinations.SELECT_VARIANTS,
                            arguments = listOf(productIdArgument)
                        ) {
                            it.arguments?.getLong(OrderCustomerDestinations.PRODUCT_ID)?.let { id ->
                                OrderSelectVariants(
                                    productId = id,
                                    viewModel = productViewModel,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onAddToBucketClicked = {
                                        lifecycleScope.launch {
                                            orderViewModel.addProductToBucket(it)
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }
                        }
                        composable(
                            OrderCustomerDestinations.SELECT_CUSTOMERS
                        ) {
                            ProvideWindowInsets(
                                windowInsetsAnimationsEnabled = true
                            ) {
                                OrderCustomerName(
                                    mainViewModel = mainViewModel,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onNewOrder = { customer, isTakeAway ->
                                        orderViewModel.newOrder(
                                            customer = customer,
                                            isTakeAway = isTakeAway
                                        )
                                        goToOrdersActivity()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToOrdersActivity() {
        val intent = Intent(this, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
