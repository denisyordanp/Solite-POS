package com.socialite.solite_pos.view.screens.orders.order_payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.data.schema.room.new_bridge.OrderPayment
import com.socialite.data.schema.room.new_bridge.OrderPromo
import com.socialite.data.schema.room.new_master.Order
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.domain.domain.GetPayments
import com.socialite.domain.domain.GetPromos
import com.socialite.domain.domain.PayOrder
import com.socialite.solite_pos.utils.config.CashAmounts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.socialite.solite_pos.schema.Payment as UiPayment
import com.socialite.solite_pos.schema.Promo as UiPromo

@HiltViewModel
class OrderPaymentViewModel @Inject constructor(
    private val getPayments: GetPayments,
    private val getPromos: GetPromos,
    private val getOrderWithProduct: GetOrderWithProduct,
    private val payOrder: PayOrder,
) : ViewModel() {

    private val _viewState = MutableStateFlow(OrderPaymentViewState.idle())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getPayments(Payment.filter(Payment.ACTIVE)),
                getPromos(Promo.filter(Promo.Status.ACTIVE))
            ) { payments, promos ->
                _viewState.value.copy(
                    promos = promos.map { com.socialite.solite_pos.schema.Promo.fromData(it) },
                    payments = payments.map { com.socialite.solite_pos.schema.Payment.fromData(it) }
                )
            }.collect(_viewState)
        }
    }

    fun getOrder(orderId: String) {
        viewModelScope.launch {
            getOrderWithProduct(orderId)
                .map {
                    _viewState.value.copy(
                        orderWithProduct = it
                    )
                }.collect(_viewState)
        }
    }

    fun addCashInput(cash: Long, total: Long) {
        viewModelScope.launch {
            val input = Pair(cash, total)
            if (input.first != 0L) {
                val currentCash = CashAmounts.generateCash(input.second).filter {
                    it.toString().startsWith(input.first.toString(), ignoreCase = true)
                            && it >= input.second
                }
                _viewState.emit(
                    _viewState.value.copy(
                        cashSuggestion = currentCash
                    )
                )
            } else {
                _viewState.emit(
                    _viewState.value.copy(
                        cashSuggestion = null
                    )
                )
            }
        }
    }

    fun payOrder(order: Order, payment: UiPayment, pay: Long, promo: UiPromo?, totalPromo: Long?) {
        viewModelScope.launch {
            val newPromo = if (promo != null && totalPromo != null) {
                OrderPromo.newPromo(
                    orderId = order.id,
                    promo = promo.id,
                    totalPromo = totalPromo
                )
            } else null

            payOrder.invoke(
                order = order,
                payment = OrderPayment.createNew(
                    order = order.id,
                    payment = payment.id,
                    pay = pay
                ),
                promo = newPromo
            )
        }
    }
}
