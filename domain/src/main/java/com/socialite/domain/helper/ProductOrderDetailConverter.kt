package com.socialite.domain.helper

import com.socialite.domain.schema.ProductOrderDetail
import com.socialite.schema.database.new_bridge.OrderDetail
import com.socialite.data.repository.OrderDetailsRepository
import com.socialite.data.repository.ProductsRepository
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
            val product = productsRepository.getProductById(item2.product).firstOrNull()?.toDomain()
            val variants = orderDetailRepository.getOrderDetailWithVariants(item2.id)
            products.add(
                ProductOrderDetail.createProduct(
                    product,
                    variants.options.map { it.toDomain() },
                    item2.amount
                )
            )
        }
        return products
    }
}
