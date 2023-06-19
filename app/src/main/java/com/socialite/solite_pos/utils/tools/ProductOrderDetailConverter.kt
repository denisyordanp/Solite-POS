package com.socialite.solite_pos.utils.tools

import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.firstOrNull

class ProductOrderDetailConverter(
    private val orderDetailRepository: OrderDetailsRepository,
    private val productsRepository: ProductsRepository,
) {
    suspend fun convert(listDetail: List<OrderDetail>): List<ProductOrderDetail> {
        val products = mutableListOf<ProductOrderDetail>()
        for (item2 in listDetail) {
            val product = productsRepository.getProductById(item2.product).firstOrNull()
            val variants = orderDetailRepository.getOrderDetailWithVariants(item2.id)
            products.add(
                ProductOrderDetail.createProduct(
                    product,
                    variants.options,
                    item2.amount
                )
            )
        }
        return products
    }
}
