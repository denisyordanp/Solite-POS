package com.socialite.solite_pos.view.screens.orders.order_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.domain.domain.GetOrderWithProduct
import com.socialite.data.schema.room.new_master.Order
import com.socialite.domain.domain.UpdateOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val updateOrder: UpdateOrder,
    private val getOrderWithProduct: GetOrderWithProduct
) : ViewModel() {

    fun getOrder(orderId: String) = getOrderWithProduct(orderId)

    fun putBackOrder(order: Order) {
        viewModelScope.launch {
            updateOrder.invoke(
                order.copy(
                    status = Order.ON_PROCESS
                )
            )
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            updateOrder.invoke(order)
        }
    }

    fun doneOrder(order: Order) {
        viewModelScope.launch {
            updateOrder.invoke(
                order.copy(
                    status = Order.NEED_PAY
                )
            )
        }
    }

    fun cancelOrder(order: Order) {
        viewModelScope.launch {
            updateOrder.invoke(
                order.copy(
                    status = Order.CANCEL
                )
            )
        }
    }
}
