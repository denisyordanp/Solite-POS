package com.socialite.solite_pos.view.order_customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicAlertDialog
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.store.StoreDestinations
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import kotlinx.coroutines.launch

class OrderCustomerActivity : SoliteActivity() {

    private lateinit var orderCustomerViewModel: OrderCustomerViewModel

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentDate = DateUtils.currentDate
        orderCustomerViewModel = OrderCustomerViewModel.getOrderCustomerViewModel(this)
        orderCustomerViewModel.loadBadges(currentDate)

        setContent {
            SolitePOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val state = orderCustomerViewModel.viewState.collectAsState().value

                    NavHost(
                        navController = navController,
                        startDestination = OrderCustomerDestinations.SELECT_ITEMS
                    ) {
                        composable(
                            OrderCustomerDestinations.SELECT_ITEMS
                        ) {
                            OrderSelectItems(
                                badges = state.badges,
                                products = state.allProducts,
                                bucketOrderState = state.bucketOrderState,
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
                                                orderCustomerViewModel.addProductToBucket(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            } else {
                                                orderCustomerViewModel.decreaseProduct(
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
                                },
                                onRemoveProduct = {
                                    orderCustomerViewModel.removeProductFromBucket(it)
                                }
                            )
                        }

                        val productIdArgument =
                            navArgument(name = OrderCustomerDestinations.PRODUCT_ID) {
                                type = NavType.StringType
                            }
                        composable(
                            route = OrderCustomerDestinations.SELECT_VARIANTS,
                            arguments = listOf(productIdArgument)
                        ) {
                            it.arguments?.getString(OrderCustomerDestinations.PRODUCT_ID)
                                ?.let { id ->
                                    LaunchedEffect(key1 = id) {
                                        orderCustomerViewModel.loadProductForSelectingVariants(id)
                                    }

                                    OrderSelectVariants(
                                        state = state.orderSelectVariantsState,
                                        onBackClicked = {
                                            orderCustomerViewModel.clearProductForSelectingVariants()
                                            navController.popBackStack()
                                        },
                                        onAddToBucketClicked = {
                                            lifecycleScope.launch {
                                                orderCustomerViewModel.addProductToBucket(it)
                                                navController.popBackStack()
                                            }
                                        },
                                        onOptionSelected = { prevOption, option, isSelected ->
                                            orderCustomerViewModel.optionSelected(
                                                prevOption,
                                                option,
                                                isSelected
                                            )
                                        },
                                        onAmountClicked = { isAdd ->
                                            orderCustomerViewModel.onAmountClicked(isAdd)
                                        },
                                    )
                                }
                        }
                        composable(
                            OrderCustomerDestinations.SELECT_CUSTOMERS
                        ) {
                            ProvideWindowInsets(
                                windowInsetsAnimationsEnabled = true
                            ) {
                                LaunchedEffect(key1 = Unit) {
                                    orderCustomerViewModel.loadCustomers()
                                }

                                OrderCustomerName(
                                    state = state.orderCustomerNameState,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onNewOrder = { customer, isTakeAway ->
                                        orderCustomerViewModel.newOrder(
                                            customer = customer,
                                            isTakeAway = isTakeAway
                                        )
                                        goToOrdersActivity()
                                    },
                                    onNewCustomer = {
                                        orderCustomerViewModel.newCustomer(it)
                                    },
                                )
                            }
                        }
                    }

                    if (state.isShouldSelectStore) {
                        BasicAlertDialog(
                            titleText = stringResource(R.string.unselect_store_title),
                            descText = stringResource(R.string.please_select_store_first_before_do_a_transaction),
                            positiveAction = {
                                goToSelectStore()
                            },
                            positiveText = stringResource(R.string.yes),
                        )
                    }
                }
            }
        }
    }

    private fun goToSelectStore() {
        val intent =
            StoreActivity.createInstanceWithDirectPage(this, StoreDestinations.MASTER_STORES)
        startActivity(intent)
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
