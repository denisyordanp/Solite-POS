package com.socialite.solite_pos.view.main.opening.store

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.socialite.solite_pos.view.main.menu.master.ListMasterActivity
import com.socialite.solite_pos.view.main.menu.master.product.ProductMasterActivity
import com.socialite.solite_pos.view.main.menu.purchase.PurchaseActivity
import com.socialite.solite_pos.view.main.opening.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.main.opening.orders.OrdersActivity
import com.socialite.solite_pos.view.main.opening.recap.RecapActivity
import com.socialite.solite_pos.view.main.opening.ui.GeneralMenus
import com.socialite.solite_pos.view.main.opening.ui.MasterMenus
import com.socialite.solite_pos.view.main.opening.ui.StoreMenus
import com.socialite.solite_pos.view.main.opening.ui.theme.SolitePOSTheme

@ExperimentalMaterialApi
class StoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = StoreDestinations.MAIN_STORE
                ) {
                    composable(StoreDestinations.MAIN_STORE) {
                        MainStoreMenu(
                            onGeneralMenuClicked = {
                                when (it) {
                                    GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                                    GeneralMenus.ORDERS -> goToOrdersActivity()
                                    GeneralMenus.SETTING -> TODO()
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            },
                            onMasterMenuClicked = {
                                when (it) {
                                    MasterMenus.PRODUCT -> goToProductMasterActivity()
                                    MasterMenus.CATEGORY -> goToCategoryActivity()
                                    MasterMenus.VARIANT -> goToVariantActivity()
                                    MasterMenus.PAYMENT -> goToPaymentActivity()
                                    MasterMenus.SUPPLIER -> goToSupplierActivity()
                                }
                            },
                            onStoreMenuClicked = {
                                when (it) {
                                    StoreMenus.SALES_RECAP -> goToRecapActivity()
                                    StoreMenus.PURCHASE -> goToPurchaseActivity()
                                    StoreMenus.OUTCOMES -> {}
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun goToRecapActivity() {
        val intent = Intent(this, RecapActivity::class.java)
        startActivity(intent)
    }

    @ExperimentalMaterialApi
    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
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

    private fun goToSupplierActivity() {
        val intent = Intent(this, ListMasterActivity::class.java)
        intent.putExtra(ListMasterActivity.TYPE, ListMasterActivity.SUPPLIER)
        startActivity(intent)
    }

    private fun goToPurchaseActivity() {
        val intent = Intent(this, PurchaseActivity::class.java)
        startActivity(intent)
    }
}
