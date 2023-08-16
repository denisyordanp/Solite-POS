package com.socialite.solite_pos.utils.tools

import com.socialite.solite_pos.data.schema.helper.ProductOrderDetail
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.repository.ProductsRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductOrderDetailConverter @Inject constructor(
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
