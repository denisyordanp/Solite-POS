package com.socialite.solite_pos.view.screens.orders

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.ActivityCompat
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
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.printer.PrinterConnection
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.screens.bluetooth.BluetoothDevicesActivity
import com.socialite.solite_pos.view.screens.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.screens.order_customer.select_items.SelectItemsScreen
import com.socialite.solite_pos.view.screens.order_customer.select_variant.SelectVariantsScreen
import com.socialite.solite_pos.view.screens.orders.order_detail.OrderDetailScreen
import com.socialite.solite_pos.view.screens.orders.order_items.OrderItemsScreen
import com.socialite.solite_pos.view.screens.orders.order_payment.OrderPaymentScreen
import com.socialite.solite_pos.view.screens.settings.SettingsActivity
import com.socialite.solite_pos.view.screens.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersActivity : SoliteActivity() {

    private val ordersViewModel: OrdersViewModel by viewModels()

    companion object {

        private const val EXTRA_REPORT = "extra_report"
        fun createInstanceForRecap(context: Context, parameters: ReportParameter) {
            val intent = Intent(context, OrdersActivity::class.java)
            intent.putExtra(EXTRA_REPORT, parameters)
            context.startActivity(intent)
        }
    }


    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    @ExperimentalCoroutinesApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        printBill = PrintBill(this)
        val date = getExtraReport() ?: ReportParameter.createTodayOnly()

        setContent {
            SolitePOSTheme {
                val navController = rememberNavController()
                val state = ordersViewModel.viewState.collectAsState().value

                NavHost(
                    navController = navController,
                    startDestination = OrderDetailDestinations.ORDERS
                ) {
                    composable(
                        route = OrderDetailDestinations.ORDERS
                    ) {
                        OrderItemsScreen(
                            parameters = date,
                            defaultTabPage = state.defaultTabPage,
                            onGeneralMenuClicked = {
                                when (it) {
                                    GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                                    GeneralMenus.STORE -> goToStoreActivity()
                                    GeneralMenus.SETTING -> goToSettingsActivity()
                                    else -> {
                                        // Do nothing
                                    }
                                }
                            },
                            onOrderClicked = {
                                navController.navigate(OrderDetailDestinations.orderDetail(it))
                            },
                            onBackClicked = {
                                onBackPressed()
                            }
                        )
                    }

                    val orderIdArgument = navArgument(name = OrderDetailDestinations.ORDER_ID) {
                        type = NavType.StringType
                    }
                    composable(
                        route = OrderDetailDestinations.ORDER_DETAIL,
                        arguments = listOf(orderIdArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.ORDER_ID)?.let { orderId ->
                            OrderDetailScreen(
                                orderId = orderId,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                timePicker = buildTimePicker(),
                                datePicker = buildDatePicker(),
                                fragmentManager = supportFragmentManager,
                                onButtonClicked = { buttonType, orderWithProduct ->
                                    when (buttonType) {
                                        OrderButtonType.PRINT -> {
                                            lifecycleScope.launch {
                                                if (orderWithProduct != null) {
                                                    printBill(
                                                        order = orderWithProduct,
                                                    )
                                                }
                                            }
                                        }

                                        OrderButtonType.QUEUE -> {
                                            lifecycleScope.launch {
                                                // TODO: Create print queue function
//                                                if (orderWithProduct != null) {
//                                                    printBill?.doPrintQueue(
//                                                        order = orderWithProduct,
//                                                        callback = {}
//                                                    )
//                                                }
                                            }
                                        }

                                        OrderButtonType.PAYMENT -> {
                                            navController.navigate(
                                                OrderDetailDestinations.orderPayment(
                                                    orderId
                                                )
                                            )
                                        }

                                        OrderButtonType.DONE -> {
                                            ordersViewModel.setDefaultPage(OrderMenus.NOT_PAY_YET.status)
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }

                                        OrderButtonType.CANCEL -> {
                                            ordersViewModel.setDefaultPage(OrderMenus.CANCELED.status)
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }

                                        OrderButtonType.PUT_BACK -> {
                                            ordersViewModel.setDefaultPage(OrderMenus.CURRENT_ORDER.status)
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                },
                                onProductsClicked = {
                                    ordersViewModel.createBucketForEdit(orderId)
                                    navController.navigate(
                                        OrderDetailDestinations.orderEditProducts(orderId)
                                    )
                                }
                            )
                        }
                    }
                    composable(
                        route = OrderDetailDestinations.ORDER_PAYMENT,
                        arguments = listOf(orderIdArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.ORDER_ID)?.let { orderId ->
                            ProvideWindowInsets(
                                windowInsetsAnimationsEnabled = true
                            ) {
                                OrderPaymentScreen(
                                    orderId = orderId,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onPayClicked = {
                                        lifecycleScope.launch {
                                            ordersViewModel.setDefaultPage(OrderMenus.DONE.status)
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                    composable(
                        route = OrderDetailDestinations.ORDER_EDIT_PRODUCTS,
                        arguments = listOf(orderIdArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.ORDER_ID)?.let { orderId ->
                            SelectItemsScreen(
                                onRemoveProduct = { product ->
                                    ordersViewModel.removeProductFromBucket(product)
                                },
                                bucketOrder = state.bucketOrder,
                                onItemClick = { product, isAdd, hasVariant ->
                                    lifecycleScope.launch {
                                        if (hasVariant) {
                                            navController.navigate(
                                                OrderDetailDestinations.orderSelectVariants(
                                                    product.id
                                                )
                                            )
                                        } else {
                                            if (isAdd) {
                                                ordersViewModel.addProductToBucket(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            } else {
                                                ordersViewModel.decreaseProduct(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            }
                                        }
                                    }
                                },
                                onClickOrder = {
                                    ordersViewModel.updateOrderProducts(orderId)
                                    navController.popBackStack()
                                },
                                onBackClicked = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                    val productIdArgument =
                        navArgument(name = OrderDetailDestinations.PRODUCT_ID) {
                            type = NavType.StringType
                        }
                    composable(
                        route = OrderDetailDestinations.ORDER_SELECT_VARIANTS,
                        arguments = listOf(productIdArgument)
                    ) {
                        it.arguments?.getString(OrderDetailDestinations.PRODUCT_ID)?.let { id ->
                            SelectVariantsScreen(
                                productId = id,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onAddToBucketClicked = {
                                    lifecycleScope.launch {
                                        ordersViewModel.addProductToBucket(it)
                                        navController.popBackStack()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun printBill(order: OrderWithProduct) {
        val defaultAddress = ordersViewModel.defaultPrinterAddress

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED && defaultAddress != null
        ) {
            PrinterConnection.getSocketFromAddress(defaultAddress) { socket ->
                if (socket != null) {
                    PrintBill.doPrint(
                        socket = socket,
                        order = order,
                        type = PrintBill.PrintType.BILL,
                        onFinished = {
                            socket.close()
                        })
                } else {
                    toBluetoothDevice(orderId = order.orderData.order.id)
                }
            }
        } else {
            toBluetoothDevice(orderId = order.orderData.order.id)
        }
    }

    private fun toBluetoothDevice(orderId: String) {
        val intent = BluetoothDevicesActivity.createIntent(this, orderId, PrintBill.PrintType.BILL)
        startActivity(intent)
    }

    private fun buildTimePicker() = MaterialTimePicker.Builder()
        .setTitleText(R.string.select_time)

    private fun buildDatePicker() = MaterialDatePicker.Builder.datePicker()
        .setCalendarConstraints(buildConstraint())
        .setTitleText(R.string.select_date)
        .setPositiveButtonText(R.string.select)

    private fun buildConstraint() = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())
        .build()

    private fun getExtraReport() = intent.getSerializableExtra(EXTRA_REPORT) as? ReportParameter

    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
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
