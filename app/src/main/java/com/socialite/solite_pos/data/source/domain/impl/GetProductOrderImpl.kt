package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class GetProductOrderImpl(
    private val orderDetailRepository: OrderDetailsRepository,
    private val productsRepository: ProductsRepository,
) : GetProductOrder {
    override fun invoke(orderId: String): Flow<List<ProductOrderDetail>> {
        return orderDetailRepository.getOrderDetailByIdOrder(orderId)
            .map {
                handleListDetail(it)
            }
    }

    private suspend fun handleListDetail(listDetail: List<OrderDetail>): List<ProductOrderDetail> {
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
