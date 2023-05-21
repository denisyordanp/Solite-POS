package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductOrderImpl(
    private val dao: OrdersDao,
    private val productsDao: ProductsDao,
) : GetProductOrder {
    override fun invoke(orderId: String): Flow<List<ProductOrderDetail>> {
        return dao.getDetailOrders(orderId)
            .map {
                handleListDetail(it)
            }
    }

    private suspend fun handleListDetail(listDetail: List<OrderDetail>): List<ProductOrderDetail> {
        val products = mutableListOf<ProductOrderDetail>()
        for (item2 in listDetail) {
            val product = productsDao.getProduct(item2.product)
            val variants = dao.getOrderVariants(item2.id)
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
