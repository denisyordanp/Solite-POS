package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetIncomesRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Order.Companion.DONE
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

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

    private val _currentBucket = MutableStateFlow(BucketOrder.idle())
    val currentBucket = _currentBucket.asStateFlow()

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

    fun addProductToBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val currentProducts = _currentBucket.value.products?.toMutableList()
            val newBucket = if(_currentBucket.value.isIdle()) {
                BucketOrder(
                    time = Calendar.getInstance().timeInMillis,
                    products = listOf(detail)
                )
            } else {
                val existingDetail = currentProducts?.find { it.product?.id == detail.product?.id }
                if (existingDetail != null) {
                    currentProducts.remove(existingDetail)
                    currentProducts.add(existingDetail.copy(
                        amount = existingDetail.amount+1
                    ))
                }

                _currentBucket.value.copy(
                    products = currentProducts!!.apply { add(detail) }
                )
            }

            _currentBucket.value = newBucket
        }
    }

    fun removeProductFromBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val currentProducts = _currentBucket.value.products?.toMutableList()

            val existingDetail = currentProducts?.find { it.product?.id == detail.product?.id }
            if (existingDetail != null) {
                currentProducts.remove(existingDetail)
            }

            if (currentProducts.isNullOrEmpty()) {
                _currentBucket.value = BucketOrder.idle()
            } else {
                _currentBucket.value = _currentBucket.value.copy(
                    products = currentProducts
                )
            }
        }
    }
}
