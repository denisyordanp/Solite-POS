package com.socialite.solite_pos.view.store

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.main.menu.master.ListMasterActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.outcomes.OutcomesActivity
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.product.ProductDetailMaster
import com.socialite.solite_pos.view.store.product.ProductsMaster
import com.socialite.solite_pos.view.store.recap.RecapMainView
import com.socialite.solite_pos.view.store.stores.StoresView
import com.socialite.solite_pos.view.store.variants.VariantView
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.MasterMenus
import com.socialite.solite_pos.view.ui.StoreMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.launch

class StoreActivity : SoliteActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var productViewModel: ProductViewModel

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)
        productViewModel = ProductViewModel.getMainViewModel(this)

        val date = DateUtils.currentDate

        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = StoreDestinations.MAIN_STORE
                ) {
                    composable(StoreDestinations.MAIN_STORE) {
                        MainStoreMenu(
                            orderViewModel = orderViewModel,
                            currentDate = date,
                            onGeneralMenuClicked = {
                                when (it) {
                                    GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                                    GeneralMenus.ORDERS -> goToOrdersActivity()
                                    GeneralMenus.SETTING -> goToSettingsActivity()
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            },
                            onMasterMenuClicked = {
                                when (it) {
                                    MasterMenus.PRODUCT -> {
                                        navController.navigate(StoreDestinations.MASTER_PRODUCT)
                                    }
                                    MasterMenus.CATEGORY -> goToCategoryActivity()
                                    MasterMenus.VARIANT -> {
                                        navController.navigate(StoreDestinations.MASTER_VARIANTS)
                                    }
//                                    MasterMenus.SUPPLIER -> goToSupplierActivity()
                                }
                            },
                            onStoreMenuClicked = {
                                when (it) {
                                    StoreMenus.SALES_RECAP -> {
                                        navController.navigate(StoreDestinations.MASTER_RECAP)
                                    }
//                                    StoreMenus.PURCHASE -> goToPurchaseActivity()
                                    StoreMenus.OUTCOMES -> {
                                        OutcomesActivity.createInstanceForRecap(this@StoreActivity)
                                    }

                                    StoreMenus.PAYMENT -> goToPaymentActivity()
                                    StoreMenus.STORE -> {
                                        navController.navigate(StoreDestinations.MASTER_STORES)
                                    }

                                    else -> {
                                        // Do nothing
                                    }
                                }
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_STORES) {
                        ProvideWindowInsets(
                            windowInsetsAnimationsEnabled = true
                        ) {
                            StoresView(
                                mainViewModel = mainViewModel,
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    composable(StoreDestinations.MASTER_RECAP) {
                        RecapMainView(
                            mainViewModel = mainViewModel,
                            orderViewModel = orderViewModel,
                            datePicker = buildRecapDatePicker(),
                            fragmentManager = supportFragmentManager,
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onOrdersClicked = {
                                OrdersActivity.createInstanceForRecap(
                                    context = this@StoreActivity,
                                    parameters = it
                                )
                            },
                            onOutcomesClicked = {
                                OutcomesActivity.createInstanceForRecap(
                                    context = this@StoreActivity,
                                    parameters = it
                                )
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_PRODUCT) {
                        ProductsMaster(
                            productViewModel = productViewModel,
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onItemClicked = {
                                navController.navigate(StoreDestinations.productDetail(it.id))
                            },
                            onVariantClicked = {
                                navController.navigate(StoreDestinations.productVariants(it.id))
                            },
                            onAddProductClicked = {
                                navController.navigate(StoreDestinations.newProduct())
                            }
                        )
                    }

                    val productIdArgument = navArgument(name = StoreDestinations.PRODUCT_ID) {
                        type = NavType.LongType
                    }
                    composable(
                        route = StoreDestinations.DETAIL_PRODUCT,
                        arguments = listOf(productIdArgument)
                    ) {
                        var currentId by rememberSaveable {
                            mutableStateOf(it.arguments?.getLong(StoreDestinations.PRODUCT_ID))
                        }

                        currentId?.let { id ->
                            ProductDetailMaster(
                                productViewModel = productViewModel,
                                productId = id,
                                onVariantClicked = {
                                    navController.navigate(StoreDestinations.productVariants(id))
                                },
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onCreateNewProduct = { product ->
                                    lifecycleScope.launch {
                                        val newId = productViewModel.insertProduct(product)
                                        currentId = newId
                                    }
                                }
                            )
                        }
                    }
                    composable(
                        route = StoreDestinations.PRODUCT_VARIANTS,
                        arguments = listOf(productIdArgument)
                    ) {
                        it.arguments?.getLong(StoreDestinations.PRODUCT_ID)?.let { id ->
                            VariantView(
                                productViewModel = productViewModel,
                                idProduct = id,
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    composable(StoreDestinations.MASTER_VARIANTS) {
                        VariantView(
                            productViewModel = productViewModel,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun buildRecapDatePicker() = MaterialDatePicker.Builder.dateRangePicker()
        .setCalendarConstraints(dateConstraint)
        .setTitleText(R.string.select_date)
        .setPositiveButtonText(R.string.select)
        .build()

    private val dateConstraint = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())
        .build()

    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
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

    private fun goToCategoryActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.CATEGORY)
        startActivity(intent)
    }

    private fun goToPaymentActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.PAYMENT)
        startActivity(intent)
    }
}
