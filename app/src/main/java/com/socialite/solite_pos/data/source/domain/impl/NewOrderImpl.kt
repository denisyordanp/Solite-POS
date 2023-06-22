package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.preference.SettingPreferences
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrderProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.utils.config.DateUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewOrderImpl @Inject constructor(
    private val settingPreferences: SettingPreferences,
    private val ordersRepository: OrdersRepository,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val orderProductVariantsRepository: OrderProductVariantsRepository,
    private val settingRepository: SettingRepository
) : NewOrder {
    override suspend fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    ) {
        val selectedStore = settingRepository.getNewSelectedStore().first()
        val orderData = generateOrder(customer, isTakeAway, currentTime, selectedStore)
        val orderWithProducts = OrderWithProduct(
            order = orderData,
            products = products
        )
        ordersRepository.insertOrder(orderWithProducts.order.order)
        insertOrderProduct(orderWithProducts)
        increaseOrderQueue()
    }

    private fun generateOrder(
        customer: Customer,
        isTakeAway: Boolean,
        currentTime: String,
        store: String
    ): OrderData {
        val order = Order.createNew(
            orderNo = generateOrderNo(currentTime),
            customer = customer.id,
            orderTime = currentTime,
            store = store,
            isTakeAway = isTakeAway,
        )
        return OrderData.newOrder(
            order = order,
            customer = customer
        )
    }

    private fun generateOrderNo(
        currentTime: String
    ): String {
        val time = generateOrderNoFromDate(currentTime)
        if (time != settingPreferences.orderDate) {
            saveDate(time)
        }
        val count = settingPreferences.orderCount
        return "${settingPreferences.orderDate}${generateQueueNumber(count)}"
    }

    private fun saveDate(time: String) {
        settingPreferences.orderDate = time
        settingPreferences.orderCount = 1
    }

    private fun generateOrderNoFromDate(time: String) =
        DateUtils.convertDateFromDate(time, DateUtils.DATE_ORDER_NO_FORMAT)

    private fun generateQueueNumber(i: Int): String {
        var str = i.toString()
        when (str.length) {
            1 -> str = "00$i"
            2 -> str = "0$i"
        }
        return str
    }

    private fun increaseOrderQueue() {
        settingPreferences.orderCount = settingPreferences.orderCount + 1
    }

    private suspend fun insertOrderProduct(order: OrderWithProduct) {
        for (item in order.products) {
            if (item.product != null) {

                val detail = OrderDetail.createNewOrderDetail(
                    order.order.order.id,
                    item.product.id,
                    item.amount
                )
                orderDetailsRepository.insertOrderDetail(detail)

                for (variant in item.variants) {
                    val orderVariant = OrderProductVariant.createNewOrderVariant(
                        detail.id, variant.id
                    )
                    orderProductVariantsRepository.insertOrderProductVariant(orderVariant)
                }
            }
        }
    }
}
