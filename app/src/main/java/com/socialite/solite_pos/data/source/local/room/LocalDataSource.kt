package com.socialite.solite_pos.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.socialite.solite_pos.data.source.local.entity.helper.ProductMixOrderDetail
import com.socialite.solite_pos.data.source.local.entity.helper.ProductOrderDetail

class LocalDataSource private constructor(val soliteDao: SoliteDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(soliteDao: SoliteDao): LocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = LocalDataSource(soliteDao)
            }
            return INSTANCE!!
        }
    }

    fun getProductOrder(orderNo: String): LiveData<List<ProductOrderDetail>> {
        val result: MediatorLiveData<List<ProductOrderDetail>> = MediatorLiveData()
        result.addSource(soliteDao.getDetailOrdersLiveData(orderNo)) { listDetail ->
            val products: ArrayList<ProductOrderDetail> = ArrayList()
            for (item2 in listDetail) {
                val product = soliteDao.getProduct(item2.idProduct)
                if (product.isMix) {
                    val mixes = soliteDao.getOrderVariantsMix(item2.id)
                    val mixProduct: ArrayList<ProductMixOrderDetail> = ArrayList()
                    for (mix in mixes.variantsMix) {
                        val variants = soliteDao.getOrderMixVariantsOption(mix.id)
                        mixProduct.add(
                            ProductMixOrderDetail(
                                soliteDao.getProduct(mix.idProduct),
                                ArrayList(variants.options),
                                mix.amount
                            )
                        )
                    }
                    products.add(ProductOrderDetail.createMix(product, mixProduct, item2.amount))
                } else {
                    val variants = soliteDao.getOrderVariants(item2.id)
                    products.add(
                        ProductOrderDetail.createProduct(
                            product,
                            ArrayList(variants.options),
                            item2.amount
                        )
                    )
                }
            }
            result.value = products
        }
        return result
    }
}
