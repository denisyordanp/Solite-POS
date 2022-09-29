package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.NewOrder
import com.socialite.solite_pos.data.source.local.entity.helper.OrderWithProduct
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.local.room.SoliteDao

class NewOrderImpl(
    private val dao: OrdersDao,
    private val productDao: ProductsDao,
    private val soliteDao: SoliteDao
) : NewOrder {
    override suspend fun invoke(order: OrderWithProduct) {
        dao.insertOrder(order.order.order)
        insertOrderProduct(order)
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
