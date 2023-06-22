package com.socialite.solite_pos.view.orders.order_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val getOrderWithProduct: GetOrderWithProduct
) : ViewModel() {

    fun getOrder(orderId: String) = getOrderWithProduct(orderId)

    fun putBackOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.updateOrder(
                order.copy(
                    status = Order.ON_PROCESS
                )
            )
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.updateOrder(order)
        }
    }

    fun doneOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.updateOrder(
                order.copy(
                    status = Order.NEED_PAY
                )
            )
        }
    }

    fun cancelOrder(order: Order) {
        viewModelScope.launch {
            ordersRepository.updateOrder(
                order.copy(
                    status = Order.CANCEL
                )
            )
        }
    }
}
