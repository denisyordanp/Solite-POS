package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.room.ProductVariantsDao
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import kotlinx.coroutines.flow.Flow

class ProductVariantsRepositoryImpl(
    private val dao: ProductVariantsDao
) : ProductVariantsRepository {

    companion object {
        @Volatile
        private var INSTANCE: ProductVariantsRepositoryImpl? = null

        fun getInstance(
            dao: ProductVariantsDao
        ): ProductVariantsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(ProductVariantsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ProductVariantsRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getVariantProduct(idProduct: Long, idVariantOption: Long) =
        dao.getVariantProduct(idProduct, idVariantOption)

    override suspend fun isProductHasVariants(idProduct: Long) =
        !dao.getProductVariants(idProduct).isNullOrEmpty()

    override fun getVariantProductById(idProduct: Long): Flow<VariantProduct?> =
        dao.getVariantProductById(idProduct)

    override suspend fun insertVariantProduct(data: VariantProduct) {
        dao.insertVariantProduct(data)
    }

    override suspend fun removeVariantProduct(data: VariantProduct) {
        dao.removeVariantProduct(data.idVariant, data.idProduct)
    }
}
