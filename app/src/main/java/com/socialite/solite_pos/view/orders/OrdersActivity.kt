package com.socialite.solite_pos.view.orders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.android.material.timepicker.MaterialTimePicker
import com.socialite.solite_pos.R
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.utils.printer.PrintBill
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.order_customer.OrderSelectItems
import com.socialite.solite_pos.view.order_customer.OrderSelectVariants
import com.socialite.solite_pos.view.settings.SettingsActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.OrderMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import com.socialite.solite_pos.view.viewModel.ProductViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class OrdersActivity : SoliteActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var productViewModel: ProductViewModel

    private var printBill: PrintBill? = null

    companion object {

        private const val EXTRA_REPORT = "extra_report"
        fun createInstanceForRecap(context: Context, parameters: ReportsParameter) {
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

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)
        productViewModel = ProductViewModel.getMainViewModel(this)

        printBill = PrintBill(this)

        val date = getExtraReport() ?: ReportsParameter.createTodayOnly()

        setContent {
            SolitePOSTheme {

                val navController = rememberNavController()
                var defaultTabPage by remember { mutableStateOf(0) }
                var isInitialBucket by remember { mutableStateOf(false) }

                NavHost(
                    navController = navController,
                    startDestination = OrderDetailDestinations.ORDERS
                ) {
                    composable(
                        route = OrderDetailDestinations.ORDERS
                    ) {
                        OrderItems(
                            orderViewModel = orderViewModel,
                            parameters = date,
                            defaultTabPage = defaultTabPage,
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
                            OrderDetailView(
                                orderId = orderId,
                                orderViewModel = orderViewModel,
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
                                                val store = mainViewModel.getStore()
                                                if (store != null && orderWithProduct != null) {
                                                    printBill?.doPrintBill(
                                                        order = orderWithProduct,
                                                        store = store,
                                                        callback = {}
                                                    )
                                                }
                                            }
                                        }

                                        OrderButtonType.QUEUE -> {
                                            lifecycleScope.launch {
                                                if (orderWithProduct != null) {
                                                    printBill?.doPrintQueue(
                                                        order = orderWithProduct,
                                                        callback = {}
                                                    )
                                                }
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
                                            defaultTabPage = OrderMenus.NOT_PAY_YET.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }

                                        OrderButtonType.CANCEL -> {
                                            defaultTabPage = OrderMenus.CANCELED.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }

                                        OrderButtonType.PUT_BACK -> {
                                            defaultTabPage = OrderMenus.CURRENT_ORDER.status
                                            navController.navigate(OrderDetailDestinations.ORDERS) {
                                                popUpTo(OrderDetailDestinations.ORDERS) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                },
                                onProductsClicked = {
                                    isInitialBucket = false
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
                                OrderPaymentView(
                                    orderId = orderId,
                                    orderViewModel = orderViewModel,
                                    mainViewModel = mainViewModel,
                                    onBackClicked = {
                                        navController.popBackStack()
                                    },
                                    onPayClicked = { order, payment, pay, promo, totalPromo ->
                                        lifecycleScope.launch {
                                            orderViewModel.payOrder(
                                                order = order,
                                                payment = payment,
                                                pay = pay,
                                                promo = promo,
                                                totalPromo = totalPromo
                                            )
                                            defaultTabPage = OrderMenus.DONE.status
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

                            if (!isInitialBucket) {
                                orderViewModel.createBucketForEdit(orderId)
                                isInitialBucket = true
                            }

                            OrderSelectItems(
                                productViewModel = productViewModel,
                                orderViewModel = orderViewModel,
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
                                                orderViewModel.addProductToBucket(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            } else {
                                                orderViewModel.decreaseProduct(
                                                    ProductOrderDetail.productNoVariant(product)
                                                )
                                            }
                                        }
                                    }
                                },
                                onClickOrder = {
                                    orderViewModel.updateOrderProducts(orderId)
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
                            OrderSelectVariants(
                                productId = id,
                                viewModel = productViewModel,
                                onBackClicked = {
                                    navController.popBackStack()
                                },
                                onAddToBucketClicked = {
                                    lifecycleScope.launch {
                                        orderViewModel.addProductToBucket(it)
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

    private fun buildTimePicker() = MaterialTimePicker.Builder()
        .setTitleText(R.string.select_time)

    private fun buildDatePicker() = MaterialDatePicker.Builder.datePicker()
        .setCalendarConstraints(buildConstraint())
        .setTitleText(R.string.select_date)
        .setPositiveButtonText(R.string.select)

    private fun buildConstraint() = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointBackward.now())
        .build()

    private fun getExtraReport() = intent.getSerializableExtra(EXTRA_REPORT) as? ReportsParameter

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

    override fun onDestroy() {
        printBill?.onDestroy()
        printBill = null
        super.onDestroy()
    }
}
