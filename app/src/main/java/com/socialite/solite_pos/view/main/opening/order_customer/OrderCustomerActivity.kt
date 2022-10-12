package com.socialite.solite_pos.view.main.opening.order_customer

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.socialite.solite_pos.view.main.opening.orders.OrdersActivity
import com.socialite.solite_pos.view.main.opening.store.StoreActivity
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class OrderCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val PRODUCT_ID = "product_id"
    }

    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ProductViewModel.getMainViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)

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
                                viewModel = viewModel,
                                onItemClicked = {
                                    navController.navigate("${OrderCustomerDestinations.SELECT_VARIANTS}/${it.id}")
                                },
                                onClickOrder = {
                                    navController.navigate(OrderCustomerDestinations.SELECT_CUSTOMERS)
                                },
                                onGeneralMenuClicked = {
                                    when (it) {
                                        GeneralMenus.ORDERS -> goToOrdersActivity()
                                        GeneralMenus.STORE -> goToStoreActivity()
                                        GeneralMenus.SETTING -> {}
                                        else -> {
                                            // Don nothing
                                        }
                                    }
                                }
                            )
                        }

                        val productIdArgument = navArgument(name = PRODUCT_ID) {
                            type = NavType.LongType
                        }
                        composable(
                            route = "${OrderCustomerDestinations.SELECT_VARIANTS}/{$PRODUCT_ID}",
                            arguments = listOf(productIdArgument)
                        ) {
                            it.arguments?.getLong(PRODUCT_ID)?.let {id ->
                                OrderSelectVariants(
                                    productId = id,
                                    viewModel = viewModel,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                        composable(
                            OrderCustomerDestinations.SELECT_CUSTOMERS
                        ) {
                            OrderCustomerName(
                                viewModel = mainViewModel,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onCLickName = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @ExperimentalMaterialApi
    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    @ExperimentalMaterialApi
    private fun goToOrdersActivity() {
        val intent = Intent(this, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}
