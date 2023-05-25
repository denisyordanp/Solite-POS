package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.repository.OrdersRepository

class UpdateOrderProductsImpl(
        private val ordersRepository: OrdersRepository
) : UpdateOrderProducts {
    override suspend fun invoke(orderId: String, products: List<ProductOrderDetail>) {
        ordersRepository.deleteOrderDetailAndRelated(orderId)
        insertOrderProduct(orderId, products)
    }

    private suspend fun insertOrderProduct(orderId: String, products: List<ProductOrderDetail>) {
        for (item in products) {
            if (item.product != null) {

                val detail = OrderDetail.createNewOrderDetail(orderId, item.product.id, item.amount)
                ordersRepository.insertOrderDetail(detail)

                val orderProductVariants = item.variants.map {
                    OrderProductVariant.createNewOrderVariant(detail.id, it.id)
                }

                ordersRepository.insertOrderProductVariants(orderProductVariants)
            }
        }
    }
}
