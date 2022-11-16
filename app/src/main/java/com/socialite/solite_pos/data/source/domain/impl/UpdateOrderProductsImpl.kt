package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.UpdateOrderProducts
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.SoliteDao

class UpdateOrderProductsImpl(
    private val dao: OrdersDao,
    private val soliteDao: SoliteDao
) : UpdateOrderProducts {
    override suspend fun invoke(orderNo: String, products: List<ProductOrderDetail>) {
        dao.deleteDetailOrders(orderNo)
        insertOrderProduct(orderNo, products)
    }

    private suspend fun insertOrderProduct(orderNo: String, products: List<ProductOrderDetail>) {
        for (item in products) {
            if (item.product != null) {

                val detail = OrderDetail(orderNo, item.product!!.id, item.amount)
                detail.id = dao.insertDetailOrder(detail)

                if (item.product!!.isMix) {
                    for (p in item.mixProducts) {

                        // TODO: No stock needed for now
                        // productDao.decreaseProductStock(p.product.id, p.amount)

                        val variantMix = OrderProductVariantMix(detail.id, p.product.id, p.amount)
                        variantMix.id = soliteDao.insertVariantMixOrder(variantMix)

                        for (variant in p.variants) {
                            val mixVariant = OrderMixProductVariant(variantMix.id, variant.id)
                            mixVariant.id =
                                soliteDao.insertMixVariantOrder(mixVariant)
                        }
                    }
                } else {

                    // TODO: No stock needed for now
                    // productDao.decreaseProductStock(
                    //    item.product!!.id,
                    //    (item.amount * item.product!!.portion)
                    //)

                    for (variant in item.variants) {
                        val orderVariant = OrderProductVariant(detail.id, variant.id)
                        dao.insertVariantOrder(orderVariant)
                    }
                }
            }
        }
    }
}
