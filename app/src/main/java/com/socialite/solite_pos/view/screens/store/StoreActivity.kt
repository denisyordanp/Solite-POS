package com.socialite.solite_pos.view.screens.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.socialite.common.menus.StoreMenus
import com.socialite.domain.helper.DateUtils
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.screens.managers.DateAndTimeManager
import com.socialite.solite_pos.view.screens.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.screens.orders.OrdersActivity
import com.socialite.solite_pos.view.screens.settings.SettingsActivity
import com.socialite.solite_pos.view.screens.store.categories.CategoryMasterScreen
import com.socialite.solite_pos.view.screens.store.outcomes.OutComesScreen
import com.socialite.solite_pos.view.screens.store.payments.PaymentMasterScreen
import com.socialite.solite_pos.view.screens.store.product_detail.ProductDetailScreen
import com.socialite.solite_pos.view.screens.store.product_master.ProductsMasterScreen
import com.socialite.solite_pos.view.screens.store.promo.PromoMasterScreen
import com.socialite.solite_pos.view.screens.store.recap.RecapScreen
import com.socialite.solite_pos.view.screens.store.store_user.StoreUsersScreen
import com.socialite.solite_pos.view.screens.store.stores.StoresScreen
import com.socialite.solite_pos.view.screens.store.user_detail.UserDetailScreen
import com.socialite.solite_pos.view.screens.store.variant_master.VariantMasterScreen
import com.socialite.solite_pos.view.screens.store.variant_product.VariantProductScreen
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.MasterMenus
import com.socialite.solite_pos.view.ui.extensions.LocalNavController
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreActivity : SoliteActivity() {

    private val storeViewModel: StoreViewModel by viewModels()

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
        val directPage = intent?.extras?.getString(EXTRA_PAGE)
        storeViewModel.loadData(date)

        setContent {
            SolitePOSTheme {
                CompositionLocalProvider(
                    LocalNavController provides rememberNavController()
                ) {
                    val state = storeViewModel.viewState.collectAsState().value

                    StoreNavigation(directPage = directPage, state = state)
                }
            }
        }
    }

    @OptIn(
        ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
        ExperimentalPagerApi::class
    )
    @Composable
    private fun StoreNavigation(directPage: String?, state: StoreViewState) {
        val navController = LocalNavController.current

        NavHost(
            navController = navController,
            startDestination = if (!directPage.isNullOrEmpty()) directPage else StoreDestinations.MAIN_STORE
        ) {
            composable(StoreDestinations.MAIN_STORE) {
                MainStoreMenu(
                    badges = state.badges,
                    menus = state.menus,
                    user = state.user,
                    store = state.store,
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

                            StoreMenus.STORE_USER -> {
                                navController.navigate(StoreDestinations.STORE_USERS)
                            }

                            else -> {}
                        }
                    },
                    onEditUserClicked = {
                        navController.navigate(StoreDestinations.DETAIL_USER)
                    }
                )
            }
            composable(StoreDestinations.MASTER_PAYMENT) {
                PaymentMasterScreen(
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
            composable(StoreDestinations.MASTER_PROMO) {
                PromoMasterScreen(
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
            composable(StoreDestinations.MASTER_CATEGORY) {
                CategoryMasterScreen(
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
            composable(StoreDestinations.MASTER_STORES) {
                StoresScreen(
                    onBackClicked = {
                        val pop = navController.navigateUp()
                        if (!pop) onBackPressed()
                    }
                )
            }
            composable(StoreDestinations.MASTER_RECAP) {
                RecapScreen(
                    datePicker = DateAndTimeManager.getRangeDatePickerBuilder(),
                    fragmentManager = supportFragmentManager,
                    onBackClicked = {
                        navController.navigateUp()
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
                        navController.navigateUp()
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
                        navController.navigateUp()
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
                            navController.navigateUp()
                        }
                    )
                }
            }
            composable(StoreDestinations.MASTER_VARIANTS) {
                VariantMasterScreen(
                    onBackClicked = {
                        navController.navigateUp()
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
                        navController.navigateUp()
                    }
                )
            }
            composable(StoreDestinations.STORE_USERS) {
                StoreUsersScreen(
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
            composable(StoreDestinations.DETAIL_USER) {
                UserDetailScreen {
                    navController.navigateUp()
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
