package com.socialite.domain.domain.impl

import com.socialite.domain.domain.UpdateOrderProducts
import com.socialite.data.schema.room.new_bridge.OrderDetail
import com.socialite.data.schema.room.new_bridge.OrderProductVariant
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.OrderProductVariantsRepository
import com.socialite.domain.schema.helper.ProductOrderDetail
import javax.inject.Inject

class UpdateOrderProductsImpl @Inject constructor(
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
