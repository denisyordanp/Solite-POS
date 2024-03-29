package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.common.di.IoDispatcher
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.ProductVariantsDao
import com.socialite.data.database.dao.ProductsDao
import com.socialite.data.database.dao.VariantOptionsDao
import com.socialite.data.database.dao.VariantsDao
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class ProductVariantsRepositoryImpl @Inject constructor(
    private val dao: ProductVariantsDao,
    private val variantsDao: VariantsDao,
    private val variantOptionsDao: VariantOptionsDao,
    private val productsDao: ProductsDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ProductVariantsRepository {

    override fun getVariantOptions(productId: String) = dao.getVariantProducts(productId).flowOn(dispatcher)
    override fun getAllVariantOptions() = dao.getAllVariantProducts().flowOn(dispatcher)

    override suspend fun getNeedUploadVariantProducts() = dao.getNeedUploadVariantProducts()

    override suspend fun isProductHasVariants(idProduct: String) =
        !dao.getProductVariants(idProduct).isNullOrEmpty()

    override fun getVariantsProductById(idProduct: String) = dao.getProductVariantsById(idProduct).flowOn(dispatcher)

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
