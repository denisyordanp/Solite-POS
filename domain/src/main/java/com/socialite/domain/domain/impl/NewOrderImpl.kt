package com.socialite.domain.domain.impl

import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrderProductVariantsRepository
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.data.schema.room.new_bridge.OrderDetail
import com.socialite.data.schema.room.new_bridge.OrderProductVariant
import com.socialite.data.schema.room.new_master.Store
import com.socialite.domain.domain.NewOrder
import com.socialite.domain.helper.DateUtils
import com.socialite.domain.helper.toData
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.OrderData
import com.socialite.domain.schema.OrderWithProduct
import com.socialite.domain.schema.ProductOrderDetail
import com.socialite.domain.schema.main.Customer
import com.socialite.domain.schema.main.Order
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class NewOrderImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val ordersRepository: OrdersRepository,
    private val orderDetailsRepository: OrderDetailsRepository,
    private val orderProductVariantsRepository: OrderProductVariantsRepository,
    private val storeRepository: StoreRepository
) : NewOrder {
    override suspend fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    ) {
        val selectedStoreId = settingRepository.getNewSelectedStore().first()
        val selectedStore = storeRepository.getStore(selectedStoreId)!!
        val orderData = generateOrder(customer, selectedStore, isTakeAway, currentTime)
        val orderWithProducts = OrderWithProduct(
            orderData = orderData,
            products = products
        )
        ordersRepository.insertOrder(orderWithProducts.orderData.order.toData())
        insertOrderProduct(orderWithProducts)
        increaseOrderQueue()
    }

    private fun generateOrder(
        customer: Customer,
        store: Store,
        isTakeAway: Boolean,
        currentTime: String
    ): OrderData {
        val order = Order.createNew(
            orderNo = generateOrderNo(currentTime),
            customer = customer.id,
            orderTime = currentTime,
            store = store.id,
            isTakeAway = isTakeAway,
        )
        return OrderData.newOrder(
            order = order,
            customer = customer,
            store = store.toDomain()
        )
    }

    private fun generateOrderNo(
        currentTime: String
    ): String {
        val time = generateOrderNoFromDate(currentTime)
        val lastOrderDate = settingRepository.getLastOrderDate()
        if (time != lastOrderDate) {
            saveDate(time)
        }
        val count = settingRepository.getOrderCount()
        return "${lastOrderDate}${generateQueueNumber(count)}"
    }

    private fun saveDate(time: String) {
        settingRepository.setLastOrderDate(time)
        settingRepository.setOrderCount(1)
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
        val currentCount = settingRepository.getOrderCount()
        settingRepository.setOrderCount(currentCount + 1)
    }

    private suspend fun insertOrderProduct(order: OrderWithProduct) {
        for (item in order.products) {
            if (item.product != null) {

                val detail = OrderDetail.createNewOrderDetail(
                    order.orderData.order.id,
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
