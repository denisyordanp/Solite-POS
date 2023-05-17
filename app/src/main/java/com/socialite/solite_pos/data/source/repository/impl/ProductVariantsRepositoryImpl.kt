package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.ProductVariantsDao
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ProductVariantsRepositoryImpl(
    private val dao: ProductVariantsDao,
    private val db: AppDatabase
) : ProductVariantsRepository {

    companion object {
        @Volatile
        private var INSTANCE: ProductVariantsRepositoryImpl? = null

        fun getInstance(
            dao: ProductVariantsDao,
            db: AppDatabase
        ): ProductVariantsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(ProductVariantsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ProductVariantsRepositoryImpl(dao = dao, db = db)
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

    override fun getVariantsProductById(idProduct: Long) = dao.getProductVariantsById(idProduct)

    override fun getVariantProductById(idProduct: Long): Flow<VariantProduct?> =
        dao.getVariantProductById(idProduct)

    override suspend fun insertVariantProduct(data: VariantProduct) {
        dao.insertVariantProduct(data)
    }

    override suspend fun removeVariantProduct(data: VariantProduct) {
        dao.removeVariantProduct(data.idVariantOption, data.idProduct)
    }

    override suspend fun migrateToUUID() {
        val productVariants = dao.getVariantProducts()
        db.withTransaction {
            for (productVariant in productVariants) {
                dao.updateProductVariant(productVariant.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
