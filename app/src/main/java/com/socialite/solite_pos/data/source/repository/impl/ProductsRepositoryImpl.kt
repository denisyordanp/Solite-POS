package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CategoriesDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Product as NewProduct

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
    override fun getAllProductWithCategories() = dao.getAllProductWithCategories()
    override fun getProductWithCategory(productId: String) = dao.getProductWithCategory(productId)
    override fun getProductById(productId: String) = dao.getProductAsFlow(productId)
    override suspend fun insertProduct(data: NewProduct) = dao.insertNewProduct(data)
    override suspend fun updateProduct(data: NewProduct) {
        dao.updateNewProduct(data)
    }

    override suspend fun migrateToUUID() {
        val products = dao.getProducts()
        db.withTransaction {
            for (product in products) {
                val updatedProduct = product.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateProduct(updatedProduct)

                val category = categoryDao.getCategoryById(product.category)
                if (category != null) {
                    val newProduct = NewProduct(
                        id = updatedProduct.new_id,
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
}
