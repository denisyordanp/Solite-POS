package com.socialite.solite_pos.view.screens.orders.order_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socialite.solite_pos.R
import com.socialite.solite_pos.compose.BasicEditText
import com.socialite.solite_pos.compose.BasicTopBar
import com.socialite.solite_pos.compose.PrimaryButtonView
import com.socialite.solite_pos.compose.basicDropdown
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.schema.room.new_master.Order
import com.socialite.solite_pos.data.schema.room.new_master.Payment
import com.socialite.solite_pos.data.schema.room.new_master.Promo
import com.socialite.solite_pos.utils.config.thousand
import com.socialite.solite_pos.view.ui.DropdownItem
import com.socialite.solite_pos.view.ui.ThousandAndSuggestionVisualTransformation
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
fun OrderPaymentScreen(
    orderId: String,
    currentViewModel: OrderPaymentViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onPayClicked: () -> Unit
) {
    LaunchedEffect(key1 = orderId) {
        currentViewModel.getOrder(orderId)
    }
    val state = currentViewModel.viewState.collectAsState().value

    Scaffold(
        topBar = {
            BasicTopBar(
                titleText = state.orderWithProduct?.orderData?.customer?.name ?: "",
                onBackClicked = onBackClicked
            )
        },
        content = { padding ->
            state.orderWithProduct?.let {
                PaymentContent(
                    modifier = Modifier
                        .padding(padding),
                    cashSuggestions = state.cashSuggestion,
                    promos = state.promos,
                    payments = state.payments,
                    orderWithProduct = it,
                    onPayClicked = { order, payment, pay, promo, total ->
                        currentViewModel.payOrder(
                            order = order,
                            payment = payment,
                            pay = pay,
                            promo = promo,
                            totalPromo = total
                        )
                        onPayClicked()
                    },
                    onAddCashInput = { cash, total ->
                        currentViewModel.addCashInput(cash, total)
                    }
                )
            }
        }
    )
}

@Composable
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
private fun PaymentContent(
    modifier: Modifier = Modifier,
    cashSuggestions: List<Long>?,
    orderWithProduct: OrderWithProduct,
    promos: List<Promo>,
    payments: List<Payment>,
    onPayClicked: (order: Order, payment: Payment, pay: Long, promo: Promo?, total: Long?) -> Unit,
    onAddCashInput: (cash: Long, total: Long) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var paymentExpanded by remember {
        mutableStateOf(false)
    }
    var promoExpanded by remember {
        mutableStateOf(false)
    }
    var selectedPayment by remember {
        mutableStateOf<Payment?>(null)
    }
    var selectedPromo by remember {
        mutableStateOf<Promo?>(null)
    }
    var cashAmount by remember {
        mutableStateOf(Pair(0L, false))
    }
    var manualInputPromo by remember {
        mutableStateOf<Long?>(null)
    }

    val totalPromo = selectedPromo?.calculatePromo(
        total = orderWithProduct.grandTotal,
        manualInput = manualInputPromo
    ) ?: 0L
    val grandTotal = orderWithProduct.grandTotal - totalPromo

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            promoOptions(
                promos,
                promoExpanded,
                selectedPromo,
                cashAmount,
                onHeaderClicked = {
                    promoExpanded = !promoExpanded
                },
                onSelectedItem = {
                    selectedPromo = if (it == selectedPromo) {
                        null
                    } else {
                        it as Promo
                    }
                    promoExpanded = false
                },
                onValueChange = {
                    val amount = it.toLongOrNull() ?: 0L
                    manualInputPromo = amount
                },
                onAction = {
                    keyboard?.hide()
                },
                manualInputPromo
            )

            grandTotalWithPromo(
                selectedPromo = selectedPromo,
                orderWithProduct = orderWithProduct,
                grandTotal = grandTotal
            )

            basicDropdown(
                isExpanded = paymentExpanded,
                title = R.string.select_payment,
                selectedItem = selectedPayment?.name,
                items = payments,
                onHeaderClicked = {
                    paymentExpanded = !paymentExpanded
                },
                onSelectedItem = {
                    selectedPayment = it as Payment
                    paymentExpanded = false
                }
            )

            paymentCashOption(
                payment = selectedPayment,
                totalAmount = grandTotal,
                cashAmount = cashAmount,
                cashSuggestions = cashSuggestions,
                onAmountChange = {
                    onAddCashInput(
                        it.first,
                        grandTotal
                    )
                    cashAmount = it
                }
            )
        }

        PaymentFooter(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            selectedPayment = selectedPayment,
            isEnough = cashAmount.first >= grandTotal,
            onPayClicked = {
                selectedPayment?.let {
                    onPayClicked(
                        orderWithProduct.orderData.order,
                        it,
                        cashAmount.first,
                        selectedPromo,
                        totalPromo
                    )
                }
            }
        )
    }
}

@ExperimentalComposeUiApi
private fun LazyListScope.promoOptions(
    promos: List<Promo>,
    promoExpanded: Boolean,
    selectedPromo: Promo?,
    cashAmount: Pair<Long, Boolean>,
    onHeaderClicked: () -> Unit,
    onSelectedItem: (DropdownItem) -> Unit,
    onValueChange: (String) -> Unit,
    onAction: () -> Unit,
    manualInputPromo: Long?
) {
    if (promos.isNotEmpty()) {
        basicDropdown(
            isExpanded = promoExpanded,
            title = R.string.select_promo,
            selectedItem = selectedPromo?.name,
            items = promos,
            onHeaderClicked = onHeaderClicked,
            onSelectedItem = onSelectedItem
        )
        if (selectedPromo?.isManualInput() == true) {
            item {
                BasicEditText(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)
                        .padding(16.dp),
                    value = manualInputPromo?.toString() ?: "",
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    visualTransformation = ThousandAndSuggestionVisualTransformation(
                        cashAmount.second
                    ),
                    placeHolder = stringResource(R.string.promo_amount),
                    onValueChange = onValueChange,
                    onAction = onAction
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

private fun LazyListScope.grandTotalWithPromo(
    selectedPromo: Promo?,
    orderWithProduct: OrderWithProduct,
    grandTotal: Long
) {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            selectedPromo?.let {
                Text(
                    text = "Rp. ${orderWithProduct.grandTotal.thousand()} - ${it.name}",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = "Rp. ${grandTotal.thousand()}",
                style = MaterialTheme.typography.h3
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@ExperimentalComposeUiApi
private fun LazyListScope.paymentCashOption(
    payment: Payment?,
    totalAmount: Long,
    cashAmount: Pair<Long, Boolean>,
    cashSuggestions: List<Long>?,
    onAmountChange: (Pair<Long, Boolean>) -> Unit
) {
    if (payment?.isCash == true) {
        item {
            val keyboardController = LocalSoftwareKeyboardController.current
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.surface)
                    .padding(16.dp)
            ) {
                val maxWidth = maxWidth
                Column {

                    val newAmount = if (cashAmount.first == 0L) "" else cashAmount.first.toString()
                    BasicEditText(
                        value = newAmount,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                        visualTransformation = ThousandAndSuggestionVisualTransformation(cashAmount.second),
                        placeHolder = stringResource(R.string.cash_amount),
                        onValueChange = {
                            val amount = it.toLongOrNull() ?: 0L
                            onAmountChange(Pair(amount, false))
                        },
                        onAction = {
                            keyboardController?.hide()
                        }
                    )

                    if (!cashSuggestions.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyHorizontalGrid(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .height(35.dp),
                            rows = GridCells.Adaptive(maxWidth)
                        ) {
                            items(cashSuggestions) { suggestion ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp)
                                        .background(
                                            color = MaterialTheme.colors.background,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(4.dp)
                                        .clickable {
                                            onAmountChange(Pair(suggestion, true))
                                        }
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .align(Alignment.Center),
                                        text = suggestion.thousand(),
                                        style = MaterialTheme.typography.overline
                                    )
                                }
                            }
                        }
                    }

                    Row {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 16.dp),
                            text = stringResource(R.string.cash_change),
                            style = MaterialTheme.typography.body1
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        val change = cashAmount.first - totalAmount
                        Text(
                            text = "Rp. ${change.thousand()}",
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentFooter(
    modifier: Modifier = Modifier,
    selectedPayment: Payment?,
    isEnough: Boolean,
    onPayClicked: () -> Unit
) {

    var errorText by remember {
        mutableStateOf<Int?>(null)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth(),
        elevation = 10.dp,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            errorText?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(it),
                    style = MaterialTheme.typography.body1.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            PrimaryButtonView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                buttonText = stringResource(R.string.pay),
                onClick = {
                    if (selectedPayment == null) {
                        errorText = R.string.please_select_payment
                    }
                    if (selectedPayment?.isCash == true && !isEnough) {
                        errorText = R.string.pay_amount_can_not_less_than_total_pay
                    } else {
                        onPayClicked()
                    }
                }
            )
        }
    }
}
