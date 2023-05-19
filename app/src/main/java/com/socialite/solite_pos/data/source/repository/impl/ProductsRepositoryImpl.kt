package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
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

    override fun getProductWithCategories(category: Long) = dao.getProductWithCategories(category)
    override fun getAllProductWithCategories() = dao.getAllProductWithCategories()
    override fun getProductWithCategory(productId: Long) = dao.getProductWithCategory(productId)
    override fun getProductById(productId: Long) = dao.getProductAsFlow(productId)
    override suspend fun insertProduct(data: Product) = dao.insertProduct(data)
    override suspend fun updateProduct(data: Product) {
        dao.updateProduct(data)
    }

    override suspend fun migrateToUUID() {
        val products = dao.getProducts()
        db.withTransaction {
            for (product in products) {
                dao.updateProduct(
                    product.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                )
            }
            val updatedProducts = dao.getProducts()
            for (product in updatedProducts) {
                val category = categoryDao.getCategoryById(product.category)
                if (category != null) {
                    val newProduct = NewProduct(
                        id = product.new_id,
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
}
