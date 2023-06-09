package com.socialite.solite_pos.view.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.outcomes.OutcomesActivity
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.categories.CategoryMasterView
import com.socialite.solite_pos.view.store.payments.PaymentMasterView
import com.socialite.solite_pos.view.store.product.ProductDetailMaster
import com.socialite.solite_pos.view.store.product.ProductsMaster
import com.socialite.solite_pos.view.store.promo.PromoMasterView
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

    companion object {
        private const val EXTRA_PAGE = "extra_page"
        fun createInstanceWithDirectPage(
            context: Context,
            page: String
        ): Intent {
            return Intent(context, StoreActivity::class.java).apply {
                putExtra(EXTRA_PAGE, page)
            }
        }
    }

    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)
        productViewModel = ProductViewModel.getMainViewModel(this)

        val date = DateUtils.currentDate
        val directPage = intent?.extras?.getString(EXTRA_PAGE)

        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = if (!directPage.isNullOrEmpty()) directPage else StoreDestinations.MAIN_STORE
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

                                    MasterMenus.CATEGORY -> {
                                        navController.navigate(StoreDestinations.MASTER_CATEGORY)
                                    }

                                    MasterMenus.VARIANT -> {
                                        navController.navigate(StoreDestinations.MASTER_VARIANTS)
                                    }
                                }
                            },
                            onStoreMenuClicked = {
                                when (it) {
                                    StoreMenus.SALES_RECAP -> {
                                        navController.navigate(StoreDestinations.MASTER_RECAP)
                                    }
                                    StoreMenus.OUTCOMES -> {
                                        OutcomesActivity.createInstanceForRecap(this@StoreActivity)
                                    }

                                    StoreMenus.PAYMENT -> {
                                        navController.navigate(StoreDestinations.MASTER_PAYMENT)
                                    }

                                    StoreMenus.STORE -> {
                                        navController.navigate(StoreDestinations.MASTER_STORES)
                                    }
                                    StoreMenus.PROMO -> {
                                        navController.navigate(StoreDestinations.MASTER_PROMO)
                                    }
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_PAYMENT) {
                        PaymentMasterView(
                            mainViewModel = mainViewModel,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_PROMO) {
                        PromoMasterView(
                            mainViewModel = mainViewModel,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_CATEGORY) {
                        CategoryMasterView(
                            productViewModel = productViewModel,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_STORES) {
                        StoresView(
                            mainViewModel = mainViewModel,
                            onBackClicked = {
                                val pop = navController.popBackStack()
                                if (!pop) onBackPressed()
                            }
                        )
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
                        type = NavType.StringType
                    }
                    composable(
                        route = StoreDestinations.DETAIL_PRODUCT,
                        arguments = listOf(productIdArgument)
                    ) {
                        var currentId by rememberSaveable {
                            val idFromNav = it.arguments?.getString(StoreDestinations.PRODUCT_ID) ?: ""
                            val isNewProduct = StoreDestinations.isNewProduct(idFromNav)
                            mutableStateOf(if (isNewProduct) "" else idFromNav)
                        }

                        ProductDetailMaster(
                            productViewModel = productViewModel,
                            productId = currentId,
                            onVariantClicked = {
                                navController.navigate(StoreDestinations.productVariants(currentId))
                            },
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onCreateNewProduct = { product ->
                                lifecycleScope.launch {
                                    productViewModel.insertProduct(product)
                                    currentId = product.id
                                }
                            }
                        )
                    }
                    composable(
                        route = StoreDestinations.PRODUCT_VARIANTS,
                        arguments = listOf(productIdArgument)
                    ) {
                        it.arguments?.getString(StoreDestinations.PRODUCT_ID)?.let { id ->
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
}
