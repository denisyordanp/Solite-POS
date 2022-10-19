package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SoliteRepository
import com.socialite.solite_pos.utils.config.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class OrderViewModel(
    private val repository: SoliteRepository,
    private val orderRepository: OrdersRepository,
    private val newOrder: NewOrder,
    private val getProductOrder: GetProductOrder,
    private val getRecapData: GetRecapData,
    private val payOrder: PayOrder,
) : ViewModel() {

    companion object : ViewModelFromFactory<OrderViewModel>() {
        fun getOrderViewModel(activity: FragmentActivity): OrderViewModel {
            return buildViewModel(activity, OrderViewModel::class.java)
        }
    }

    private val _currentBucket = MutableStateFlow(BucketOrder.idle())
    val currentBucket = _currentBucket.asStateFlow()

    fun getOrderList(status: Int, date: String) = orderRepository.getOrderList(status, date)

    suspend fun getOrderDetail(orderNo: String) = orderRepository.getOrderDetail(orderNo)

    suspend fun getProductOrder(orderNo: String) = getProductOrder.invoke(orderNo)

    fun getIncomes(date: String) = getRecapData(Order.DONE, date)

    suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment =
        orderRepository.insertPaymentOrder(payment)

    fun newOrder(order: OrderWithProduct) {
        viewModelScope.launch {
            newOrder.invoke(
                customer = order.order.customer,
                isTakeAway = order.order.order.isTakeAway,
                products = order.products,
                currentTime = DateUtils.currentDate
            )
        }
    }

    fun newOrderImprovement(
        customer: Customer,
        isTakeAway: Boolean,
    ) {
        viewModelScope.launch {
            _currentBucket.value.products?.let {
                newOrder.invoke(
                    customer = customer,
                    isTakeAway = isTakeAway,
                    products = it,
                    currentTime = DateUtils.currentDateTime
                )
            }
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    fun doneOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order.copy(
                status = Order.NEED_PAY
            ))
        }
    }

    fun cancelOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order.copy(
                status = Order.CANCEL
            ))
        }
    }

    fun payOrder(order: Order, payment: Payment, pay: Long) {
        viewModelScope.launch {
            payOrder.invoke(
                order = order,
                payment = OrderPayment(
                    orderNo = order.orderNo,
                    idPayment = payment.id,
                    pay = pay
                )
            )
        }
    }

    fun replaceProductOrder(old: OrderWithProduct, new: OrderWithProduct) {
        repository.replaceProductOrder(old, new)
    }

    fun addProductToBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val newBucket = if(_currentBucket.value.isIdle()) {
                BucketOrder(
                    time = Calendar.getInstance().timeInMillis,
                    products = listOf(detail)
                )
            } else {
                val currentProducts = _currentBucket.value.products!!.toMutableList()
                val existingDetail = currentProducts.findExisting(detail)
                if (existingDetail != null) {
                    currentProducts.remove(existingDetail)
                    currentProducts.add(existingDetail.copy(
                        amount = existingDetail.amount+1
                    ))
                } else {
                    currentProducts.add(detail)
                }

                _currentBucket.value.copy(
                    products = currentProducts
                )
            }

            _currentBucket.value = newBucket
        }
    }

    fun removeProductFromBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val currentProducts = _currentBucket.value.products?.toMutableList()

            val existingDetail = currentProducts?.findExisting(detail)
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

    private fun List<ProductOrderDetail>.findExisting(compare: ProductOrderDetail): ProductOrderDetail? {
        return this.find {
            it.product == compare.product &&
                    it.variants == compare.variants &&
                    it.mixProducts == compare.mixProducts
        }
    }
}
