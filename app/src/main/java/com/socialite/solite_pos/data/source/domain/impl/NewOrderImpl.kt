package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.local.room.SoliteDao
import com.socialite.solite_pos.utils.preference.OrderPref

class NewOrderImpl(
    private val dao: OrdersDao,
    private val productDao: ProductsDao,
    private val soliteDao: SoliteDao,
    private val orderPref: OrderPref
) : NewOrder {
    override suspend fun invoke(
        customer: Customer,
        isTakeAway: Boolean,
        products: List<ProductOrderDetail>,
        currentTime: String
    ) {
        val orderData = generateOrder(customer, isTakeAway, currentTime)
        val orderWithProducts = OrderWithProduct(
            order = orderData,
            products = products
        )
        dao.insertOrder(orderWithProducts.order.order)
        insertOrderProduct(orderWithProducts)
        increaseOrderQueue()
    }

    private fun generateOrder(
        customer: Customer,
        isTakeAway: Boolean,
        currentTime: String
    ): OrderData {
        val order = Order(
            orderNo = generateOrderNo(currentTime),
            customer = customer.id,
            orderTime = currentTime,
            isTakeAway = isTakeAway,
        )
        return OrderData(
            order = order,
            customer = customer
        )
    }

    private fun generateOrderNo(
        currentTime: String
    ): String {
        if (currentTime != orderPref.orderDate) {
            saveDate(currentTime)
        }
        return "${orderPref.orderDate}${generateQueueNumber(orderPref.orderCount)}"
    }

    private fun saveDate(time: String) {
        orderPref.orderDate = time
        orderPref.orderCount = 1
    }

    private fun generateQueueNumber(i: Int): String {
        var str = i.toString()
        when (str.length) {
            1 -> str = "00$i"
            2 -> str = "0$i"
        }
        return str
    }

    private fun increaseOrderQueue() {
        orderPref.orderCount = orderPref.orderCount + 1
    }

    private fun insertOrderProduct(order: OrderWithProduct) {
        for (item in order.products) {
            if (item.product != null) {

                val detail = OrderDetail(order.order.order.orderNo, item.product!!.id, item.amount)
                detail.id = dao.insertDetailOrder(detail)

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        productDao.decreaseProductStock(p.product.id, p.amount)

                        val variantMix = OrderProductVariantMix(detail.id, p.product.id, p.amount)
                        variantMix.id = soliteDao.insertVariantMixOrder(variantMix)

                        for (variant in p.variants) {
                            val mixVariant = OrderMixProductVariant(variantMix.id, variant.id)
                            mixVariant.id =
                                soliteDao.insertMixVariantOrder(mixVariant)
                        }
                    }
                } else {

                    productDao.decreaseProductStock(
                        item.product!!.id,
                        (item.amount * item.product!!.portion)
                    )

                    for (variant in item.variants) {
                        val orderVariant = OrderProductVariant(detail.id, variant.id)
                        dao.insertVariantOrder(orderVariant)
                    }
                }
            }
        }
    }
}
