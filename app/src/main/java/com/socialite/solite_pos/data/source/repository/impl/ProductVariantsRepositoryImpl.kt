package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.ProductVariantsDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.local.room.VariantsDao
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.VariantProduct as NewVariantProduct

class ProductVariantsRepositoryImpl(
    private val dao: ProductVariantsDao,
    private val variantsDao: VariantsDao,
    private val variantOptionsDao: VariantOptionsDao,
    private val productsDao: ProductsDao,
    private val db: AppDatabase
) : ProductVariantsRepository {

    companion object {
        @Volatile
        private var INSTANCE: ProductVariantsRepositoryImpl? = null

        fun getInstance(
            dao: ProductVariantsDao,
            variantsDao: VariantsDao,
            variantOptionsDao: VariantOptionsDao,
            productsDao: ProductsDao,
            db: AppDatabase
        ): ProductVariantsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(ProductVariantsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ProductVariantsRepositoryImpl(
                            dao = dao,
                            db = db,
                            variantsDao = variantsDao,
                            variantOptionsDao = variantOptionsDao,
                            productsDao = productsDao
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getVariantProduct(idProduct: String, idVariantOption: String) =
        dao.getVariantProduct(idProduct, idVariantOption)

    override suspend fun isProductHasVariants(idProduct: String) =
        !dao.getProductVariants(idProduct).isNullOrEmpty()

    override fun getVariantsProductById(idProduct: String) = dao.getProductVariantsById(idProduct)

    override fun getVariantProductById(idProduct: String) =
        dao.getVariantProductById(idProduct)

    override suspend fun insertVariantProduct(data: NewVariantProduct) {
        dao.insertNewVariantProduct(data)
    }

    override suspend fun removeVariantProduct(data: NewVariantProduct) {
        dao.removeVariantProduct(data.variantOption, data.product)
    }

    override suspend fun migrateToUUID() {
        val productVariants = dao.getVariantProducts()
        db.withTransaction {
            for (productVariant in productVariants) {
                val updatedProductVariant = productVariant.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateProductVariant(updatedProductVariant)

                val variant = variantsDao.getVariantById(productVariant.idVariant)
                val variantOption = variantOptionsDao.getVariantOptionById(productVariant.idVariantOption)
                val product = productsDao.getProductById(productVariant.idProduct)
                if (variant != null && variantOption != null && product != null) {
                    val newProductVariant = NewVariantProduct(
                        id = updatedProductVariant.new_id,
                        variant = variant.new_id,
                        variantOption = variantOption.new_id,
                        product = product.new_id,
                        isUploaded = productVariant.isUploaded
                    )
                    dao.insertNewVariantProduct(newProductVariant)
                }
            }
        }
    }

    override suspend fun deleteAllOldProductVariants() {
        dao.deleteAllOldProductVariants()
    }
}
