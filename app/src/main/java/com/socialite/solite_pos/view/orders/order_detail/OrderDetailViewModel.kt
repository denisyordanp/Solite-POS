package com.socialite.solite_pos.view.orders.order_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetOrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.di.loggedin.LoggedInDomainInjection
import com.socialite.solite_pos.di.loggedin.LoggedInRepositoryInjection
import kotlinx.coroutines.launch

class OrderDetailViewModel(
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

    companion object {
        fun getFactory(context: Context) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OrderDetailViewModel(
                    ordersRepository = LoggedInRepositoryInjection.provideOrdersRepository(context),
                    getOrderWithProduct = LoggedInDomainInjection.provideGetOrderWithProduct(context)
                ) as T
            }
        }
    }
}
