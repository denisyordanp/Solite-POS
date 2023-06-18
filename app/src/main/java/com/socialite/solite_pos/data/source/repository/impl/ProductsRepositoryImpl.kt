package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CategoriesDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import java.util.UUID

class ProductsRepositoryImpl(
    private val dao: ProductsDao,
    private val categoryDao: CategoriesDao,
    private val db: AppDatabase
) : ProductsRepository {

    companion object {
        @Volatile
        private var INSTANCE: ProductsRepositoryImpl? = null

        fun getInstance(
            dao: ProductsDao,
            categoryDao: CategoriesDao,
            db: AppDatabase
        ): ProductsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(ProductsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE =
                            ProductsRepositoryImpl(dao = dao, db = db, categoryDao = categoryDao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getProductWithCategories(category: String) = dao.getProductWithCategories(category)
    override fun getActiveProductsWithCategory() = dao.getActiveProductsWithCategory()
    override fun getAllProductsWithCategory() = dao.getAllProductsWithCategory()
    override fun getProductWithCategory(productId: String) = dao.getProductWithCategory(productId)
    override fun getProductById(productId: String) = dao.getProductAsFlow(productId)
    override suspend fun getNeedUploadProducts() = dao.getNeedUploadProducts()
    override suspend fun insertProduct(data: Product) = dao.insertNewProduct(data)
    override suspend fun insertProducts(list: List<Product>) = dao.insertProducts(list)
    override suspend fun updateProduct(data: Product) {
        dao.updateNewProduct(data.copy(
            isUploaded = false
        ))
    }

    override suspend fun migrateToUUID() {
        val products = dao.getProducts()
        db.withTransaction {
            for (product in products) {
                val uuid = product.new_id.ifEmpty {
                    val updatedProduct = product.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateProduct(updatedProduct)
                    updatedProduct.new_id
                }

                val category = categoryDao.getCategoryById(product.category)
                if (category != null) {
                    val newProduct = Product(
                        id = uuid,
                        name = product.name,
                        category = category.new_id,
                        image = product.image,
                        desc = product.desc,
                        price = product.sellPrice,
                        isActive = product.isActive,
                        isUploaded = product.isUploaded
                    )
                    dao.insertNewProduct(newProduct)
                }
            }
        }
    }

    override suspend fun deleteAllOldProducts() {
        dao.deleteAllOldProducts()
    }

    override suspend fun deleteAllNewProducts() {
        dao.deleteAllNewProducts()
    }

    override suspend fun getItems(): List<Product> {
        return dao.getNewProducts()
    }

    override suspend fun updateItems(items: List<Product>) {
        dao.updateProducts(items)
    }

    override suspend fun insertItems(items: List<Product>) {
        dao.insertProducts(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Product>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
