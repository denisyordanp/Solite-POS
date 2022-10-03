package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetIncomesRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Order.Companion.DONE
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: SoliteRepository,
    private val orderRepository: OrdersRepository,
    private val newOrder: NewOrder,
    private val getProductOrder: GetProductOrder,
    private val getIncomesRecapData: GetIncomesRecapData
) : ViewModel() {

    companion object : ViewModelFromFactory<OrderViewModel>() {
        fun getOrderViewModel(activity: FragmentActivity): OrderViewModel {
            return buildViewModel(activity, OrderViewModel::class.java)
        }
    }

    fun getOrderList(status: Int, date: String) = orderRepository.getOrderList(status, date)

    suspend fun getProductOrder(orderNo: String) = getProductOrder.invoke(orderNo)

    suspend fun getIncomes(date: String) = getIncomesRecapData(DONE, date)

    suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment =
        orderRepository.insertPaymentOrder(payment)

    fun newOrder(order: OrderWithProduct) {
        viewModelScope.launch {
            newOrder.invoke(order)
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    fun doneOrder(order: OrderWithProduct) {
        repository.doneOrder(order)
    }

    fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct) {
        repository.replaceProductOrder(old, new)
    }
}
