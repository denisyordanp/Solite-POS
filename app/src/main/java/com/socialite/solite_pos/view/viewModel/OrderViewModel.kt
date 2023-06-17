package com.socialite.solite_pos.view.viewModel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.domain.GetRecapData
import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.domain.PayOrder
import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.BucketOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import com.socialite.solite_pos.view.factory.LoggedInViewModelFromFactory
import com.socialite.solite_pos.view.ui.OrderMenus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class OrderViewModel(
    private val orderRepository: OrdersRepository,
    private val newOrder: NewOrder,
    private val getProductOrder: GetProductOrder,
    private val getRecapData: GetRecapData,
    private val payOrder: PayOrder,
    private val updateOrderProducts: UpdateOrderProducts
) : ViewModel() {

    companion object : LoggedInViewModelFromFactory<OrderViewModel>() {
        fun getOrderViewModel(activity: FragmentActivity): OrderViewModel {
            return buildViewModel(activity, OrderViewModel::class.java)
        }
    }

    private val _currentBucket = MutableStateFlow(BucketOrder.idle())
    val currentBucket = _currentBucket.asStateFlow()

    fun getOrderList(status: Int, parameters: ReportsParameter) =
        orderRepository.getOrderList(status, parameters)

    fun getOrderBadge(orderMenus: OrderMenus, parameters: ReportsParameter) = when (orderMenus) {
        OrderMenus.CURRENT_ORDER, OrderMenus.NOT_PAY_YET -> {
            orderRepository
                .getOrderList(orderMenus.status, parameters)
                .map {
                    if (it.isEmpty()) null else it.size
                }
        }

        else -> {
            flowOf(null)
        }
    }

    suspend fun getOrderDetail(orderNo: String) = orderRepository.getOrderData(orderNo)
    fun getOrderData(orderId: String) = orderRepository.getOrderDataAsFlow(orderId)

    fun getProductOrder(orderId: String) = getProductOrder.invoke(orderId)

    fun getIncomes(parameters: ReportsParameter) = getRecapData(parameters)

    fun newOrder(
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

    fun updateOrderProducts(orderNo: String) {
        viewModelScope.launch {
            _currentBucket.value.products?.let {
                updateOrderProducts.invoke(orderNo, it)
            }
        }
    }

    fun doneOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(
                order.copy(
                    status = Order.NEED_PAY
                )
            )
        }
    }

    fun cancelOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(
                order.copy(
                    status = Order.CANCEL
                )
            )
        }
    }

    fun putBackOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(
                order.copy(
                    status = Order.ON_PROCESS
                )
            )
        }
    }

    fun payOrder(order: Order, payment: Payment, pay: Long, promo: Promo?, totalPromo: Long?) {
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

    fun createBucketForEdit(orderNo: String) {
        viewModelScope.launch {
            val order = getOrderDetail(orderNo)
            order?.let {
                val productsOrder = getProductOrder(orderNo).first()

                val newBucket = BucketOrder(
                    time = DateUtils.strToDate(it.order.orderTime).time,
                    products = productsOrder
                )

                _currentBucket.value = newBucket
            }
        }
    }

    fun addProductToBucket(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val newBucket = if (_currentBucket.value.isIdle()) {
                BucketOrder(
                    time = Calendar.getInstance().timeInMillis,
                    products = listOf(detail)
                )
            } else {
                val currentProducts = _currentBucket.value.products!!.toMutableList()
                val existingDetail = currentProducts.findExisting(detail)
                if (existingDetail != null) {
                    currentProducts.remove(existingDetail)
                    currentProducts.add(
                        existingDetail.copy(
                            amount = existingDetail.amount + 1
                        )
                    )
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

    fun decreaseProduct(detail: ProductOrderDetail) {
        viewModelScope.launch {
            val currentProducts = _currentBucket.value.products?.toMutableList()

            val existingDetail = currentProducts?.findExisting(detail)
            if (existingDetail != null) {
                currentProducts.remove(existingDetail)
                if (existingDetail.amount > 1) {
                    currentProducts.add(
                        existingDetail.copy(
                            amount = existingDetail.amount - 1
                        )
                    )
                }
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
                    it.variants == compare.variants
        }
    }
}
