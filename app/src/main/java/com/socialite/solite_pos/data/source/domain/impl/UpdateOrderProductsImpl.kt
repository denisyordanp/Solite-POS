package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.OrderProductVariantsRepository

class UpdateOrderProductsImpl(
    private val orderDetailsRepository: OrderDetailsRepository,
    private val orderProductVariantsRepository: OrderProductVariantsRepository
) : UpdateOrderProducts {
    override suspend fun invoke(orderId: String, products: List<ProductOrderDetail>) {
        orderDetailsRepository.deleteOrderDetailAndRelated(orderId)
        insertOrderProduct(orderId, products)
    }

    private suspend fun insertOrderProduct(orderId: String, products: List<ProductOrderDetail>) {
        for (item in products) {
            if (item.product != null) {

                val detail = OrderDetail.createNewOrderDetail(orderId, item.product.id, item.amount)
                orderDetailsRepository.insertOrderDetail(detail)

                val orderProductVariants = item.variants.map {
                    OrderProductVariant.createNewOrderVariant(detail.id, it.id)
                }

                orderProductVariantsRepository.insertOrderProductVariants(orderProductVariants)
            }
        }
    }
}
