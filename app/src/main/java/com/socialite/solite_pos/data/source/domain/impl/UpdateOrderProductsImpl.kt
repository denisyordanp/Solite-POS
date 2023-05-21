package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.room.OrdersDao

class UpdateOrderProductsImpl(
    private val dao: OrdersDao
) : UpdateOrderProducts {
    override suspend fun invoke(orderId: String, products: List<ProductOrderDetail>) {
        dao.deleteDetailOrders(orderId)
        insertOrderProduct(orderId, products)
    }

    private suspend fun insertOrderProduct(orderId: String, products: List<ProductOrderDetail>) {
        for (item in products) {
            if (item.product != null) {

                val detail = OrderDetail.createNewOrderDetail(orderId, item.product.id, item.amount)
                dao.insertNewOrderDetail(detail)

                for (variant in item.variants) {
                    val orderVariant =
                        OrderProductVariant.createNewOrderVariant(detail.id, variant.id)
                    dao.insertNewOrderProductVariant(orderVariant)
                }
            }
        }
    }
}
