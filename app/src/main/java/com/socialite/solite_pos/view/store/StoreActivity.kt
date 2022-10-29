package com.socialite.solite_pos.view.store

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.socialite.solite_pos.view.main.menu.master.product.ProductMasterActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.outcomes.OutcomesActivity
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.product.ProductDetailMaster
import com.socialite.solite_pos.view.store.product.ProductsMaster
import com.socialite.solite_pos.view.store.recap.RecapMainView
import com.socialite.solite_pos.view.store.stores.StoresView
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.MasterMenus
import com.socialite.solite_pos.view.ui.StoreMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class StoreActivity : SoliteActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var productViewModel: ProductViewModel

    private lateinit var datePicker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)
        productViewModel = ProductViewModel.getMainViewModel(this)

        val constraint = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()
        datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setCalendarConstraints(constraint)
            .setTitleText(R.string.select_date)
            .setPositiveButtonText(R.string.select)
            .build()

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
//                                    MasterMenus.PRODUCT -> {
//                                        navController.navigate(StoreDestinations.MASTER_PRODUCT)
//                                    }
                                    MasterMenus.PRODUCT -> goToProductMasterActivity()

                                    MasterMenus.CATEGORY -> goToCategoryActivity()
                                    MasterMenus.VARIANT -> goToVariantActivity()
//                                    MasterMenus.SUPPLIER -> goToSupplierActivity()
                                }
                            },
                            onStoreMenuClicked = {
                                when (it) {
                                    StoreMenus.SALES_RECAP -> {
                                        navController.navigate(StoreDestinations.MASTER_RECAP)
                                    }
//                                    StoreMenus.PURCHASE -> goToPurchaseActivity()
                                    StoreMenus.OUTCOMES -> goToOutcomesActivity()
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
                            datePicker = datePicker,
                            fragmentManager = supportFragmentManager,
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onOrdersClicked = {
                                OrdersActivity.createInstanceForRecap(this@StoreActivity, it)
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
                        it.arguments?.getLong(StoreDestinations.PRODUCT_ID)?.let { id ->
                            ProductDetailMaster(
                                productViewModel = productViewModel,
                                productId = id,
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

    private fun goToOutcomesActivity() {
        val intent = Intent(this, OutcomesActivity::class.java)
        startActivity(intent)
    }

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

    private fun goToProductMasterActivity() {
        val intent = Intent(this, ProductMasterActivity::class.java)
        startActivity(intent)
    }

    private fun goToCategoryActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.CATEGORY)
        startActivity(intent)
    }

    private fun goToVariantActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.VARIANT)
        startActivity(intent)
    }

    private fun goToPaymentActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.PAYMENT)
        startActivity(intent)
    }
}
