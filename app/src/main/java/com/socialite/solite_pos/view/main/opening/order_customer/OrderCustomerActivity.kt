package com.socialite.solite_pos.view.main.opening.order_customer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel

class OrderCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel: ProductViewModel
    private lateinit var mainViewModel: MainViewModel

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
                                    navController.navigate(OrderCustomerDestinations.SELECT_VARIANTS)
                                },
                                onClickOrder = {
                                    navController.navigate(OrderCustomerDestinations.SELECT_CUSTOMERS)
                                }
                            )
                        }
                        composable(
                            OrderCustomerDestinations.SELECT_VARIANTS
                        ) {
                            OrderSelectVariants {
                                navController.popBackStack()
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
}
