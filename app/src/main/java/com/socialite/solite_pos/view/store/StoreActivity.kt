package com.socialite.solite_pos.view.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
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
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.managers.DateAndTimeManager
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.store.outcomes.OutComesScreen
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.categories.CategoryMasterScreen
import com.socialite.solite_pos.view.store.payments.PaymentMasterScreen
import com.socialite.solite_pos.view.store.product_detail.ProductDetailScreen
import com.socialite.solite_pos.view.store.product_master.ProductsMasterScreen
import com.socialite.solite_pos.view.store.promo.PromoMasterScreen
import com.socialite.solite_pos.view.store.recap.RecapScreen
import com.socialite.solite_pos.view.store.stores.StoresScreen
import com.socialite.solite_pos.view.store.variant_master.VariantMasterScreen
import com.socialite.solite_pos.view.store.variant_product.VariantProductScreen
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.MasterMenus
import com.socialite.solite_pos.view.ui.StoreMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import kotlinx.coroutines.launch

class StoreActivity : SoliteActivity() {

    private val storeViewModel: StoreViewModel by viewModels {
        StoreViewModel.getFactory(this)
    }

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
        val date = DateUtils.currentDate
        storeViewModel.loadBadges(date)

        setContent {
            SolitePOSTheme {
                val directPage = intent?.extras?.getString(EXTRA_PAGE)
                val navController = rememberNavController()
                val state = storeViewModel.viewState.collectAsState().value

                NavHost(
                    navController = navController,
                    startDestination = if (!directPage.isNullOrEmpty()) directPage else StoreDestinations.MAIN_STORE
                ) {
                    composable(StoreDestinations.MAIN_STORE) {
                        MainStoreMenu(
                            badges = state.badges,
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
                                        navController.navigate(
                                            StoreDestinations.outcomes(
                                                ReportParameter.createTodayOnly(true)
                                            )
                                        )
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
                        PaymentMasterScreen(
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_PROMO) {
                        PromoMasterScreen(
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_CATEGORY) {
                        CategoryMasterScreen(
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_STORES) {
                        StoresScreen(
                            onBackClicked = {
                                val pop = navController.popBackStack()
                                if (!pop) onBackPressed()
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_RECAP) {
                        RecapScreen(
                            datePicker = DateAndTimeManager.getRangeDatePickerBuilder(),
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
                                navController.navigate(
                                    StoreDestinations.outcomes(it)
                                )
                            }
                        )
                    }
                    composable(StoreDestinations.MASTER_PRODUCT) {
                        ProductsMasterScreen(
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
                            val idFromNav =
                                it.arguments?.getString(StoreDestinations.PRODUCT_ID) ?: ""
                            val isNewProduct = StoreDestinations.isNewProduct(idFromNav)
                            mutableStateOf(if (isNewProduct) "" else idFromNav)
                        }

                        ProductDetailScreen(
                            productId = currentId,
                            onVariantClicked = {
                                navController.navigate(StoreDestinations.productVariants(currentId))
                            },
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onCreateNewProduct = { product ->
                                lifecycleScope.launch {
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
                            VariantProductScreen(
                                productId = id,
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    composable(StoreDestinations.MASTER_VARIANTS) {
                        VariantMasterScreen(
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = StoreDestinations.OUTCOMES,
                        arguments = ReportParameter.getArguments()
                    ) {
                        val report = ReportParameter.createReportFromArguments(it.arguments)
                        OutComesScreen(
                            timePicker = DateAndTimeManager.getTimePickerBuilder(),
                            datePicker = DateAndTimeManager.getDatePickerBuilder(),
                            fragmentManager = supportFragmentManager,
                            parameters = report,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
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
}
