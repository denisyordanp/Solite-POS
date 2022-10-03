package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductOrder
import com.socialite.solite_pos.data.source.local.entity.helper.ProductMixOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductOrderImpl(
    private val dao: OrdersDao,
    private val productsDao: ProductsDao,
) : GetProductOrder {
    override suspend fun invoke(orderNo: String): Flow<List<ProductOrderDetail>> {
        return dao.getDetailOrdersLiveData(orderNo)
            .map {
                handleListDetail(it)
            }
    }

    private suspend fun handleListDetail(listDetail: List<OrderDetail>): List<ProductOrderDetail> {
        val products: ArrayList<ProductOrderDetail> = ArrayList()
        for (item2 in listDetail) {
            val product = productsDao.getProduct(item2.idProduct)
            if (product.isMix) {
                val mixes = dao.getOrderVariantsMix(item2.id)
                val mixProduct: ArrayList<ProductMixOrderDetail> = ArrayList()
                for (mix in mixes.variantsMix) {
                    val variants = dao.getOrderMixVariantsOption(mix.id)
                    mixProduct.add(
                        ProductMixOrderDetail(
                            productsDao.getProduct(mix.idProduct),
                            ArrayList(variants.options),
                            mix.amount
                        )
                    )
                }
                products.add(ProductOrderDetail.createMix(product, mixProduct, item2.amount))
            } else {
                val variants = dao.getOrderVariants(item2.id)
                products.add(
                    ProductOrderDetail.createProduct(
                        product,
                        ArrayList(variants.options),
                        item2.amount
                    )
                )
            }
        }
        return products
    }
}
