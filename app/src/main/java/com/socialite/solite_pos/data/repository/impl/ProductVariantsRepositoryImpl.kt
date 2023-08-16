package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.new_bridge.VariantProduct
import com.socialite.solite_pos.database.AppDatabase
import com.socialite.solite_pos.database.dao.ProductVariantsDao
import com.socialite.solite_pos.database.dao.ProductsDao
import com.socialite.solite_pos.database.dao.VariantOptionsDao
import com.socialite.solite_pos.database.dao.VariantsDao
import com.socialite.solite_pos.data.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import java.util.UUID
import javax.inject.Inject

class ProductVariantsRepositoryImpl @Inject constructor(
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

    override fun getVariantOptions(productId: String) = dao.getVariantProducts(productId)
    override fun getAllVariantOptions() = dao.getAllVariantProducts()

    override suspend fun getNeedUploadVariantProducts() = dao.getNeedUploadVariantProducts()

    override suspend fun isProductHasVariants(idProduct: String) =
        !dao.getProductVariants(idProduct).isNullOrEmpty()

    override fun getVariantsProductById(idProduct: String) = dao.getProductVariantsById(idProduct)

    override suspend fun insertVariantProduct(data: VariantProduct) {
        dao.insertNewVariantProduct(data)
    }

    override suspend fun getProductVariantIds() = dao.getProductVariantIds()

    override suspend fun removeVariantProduct(data: VariantProduct) {
        dao.updateNewVariantProduct(
            data.copy(
                isDeleted = true
            )
        )
    }

    override suspend fun migrateToUUID() {
        val productVariants = dao.getVariantProducts()
        db.withTransaction {
            for (productVariant in productVariants) {
                val uuid = productVariant.new_id.ifEmpty {
                    val updatedProductVariant = productVariant.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateProductVariant(updatedProductVariant)
                    updatedProductVariant.new_id
                }

                val variant = variantsDao.getVariantById(productVariant.idVariant)
                val variantOption =
                    variantOptionsDao.getVariantOptionById(productVariant.idVariantOption)
                val product = productsDao.getProductById(productVariant.idProduct)
                if (variant != null && variantOption != null && product != null) {
                    val newProductVariant = VariantProduct(
                        id = uuid,
                        variant = variant.new_id,
                        variantOption = variantOption.new_id,
                        product = product.new_id,
                        isUploaded = productVariant.isUploaded,
                        isDeleted = false
                    )
                    dao.insertNewVariantProduct(newProductVariant)
                }
            }
        }
    }

    override suspend fun deleteAllOldProductVariants() {
        dao.deleteAllOldProductVariants()
    }

    override suspend fun deleteAllNewProductVariants() {
        dao.deleteAllNewProductVariants()
    }

    override suspend fun deleteAllDeletedProductVariants() {
        dao.deleteAllDeletedProductVariants()
    }

    override suspend fun getItems(): List<VariantProduct> {
        return dao.getNewVariantProducts()
    }

    override suspend fun updateItems(items: List<VariantProduct>) {
        dao.updateVariantProducts(items)
    }

    override suspend fun insertItems(items: List<VariantProduct>) {
        dao.insertVariantProducts(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<VariantProduct>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
