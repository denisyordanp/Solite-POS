package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import java.util.UUID

class ProductsRepositoryImpl(
    private val dao: ProductsDao,
    private val db: AppDatabase
) : ProductsRepository {

    companion object {
        @Volatile
        private var INSTANCE: ProductsRepositoryImpl? = null

        fun getInstance(
            dao: ProductsDao,
            db: AppDatabase
        ): ProductsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(ProductsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ProductsRepositoryImpl(dao = dao, db = db)
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
                dao.updateProduct(product.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
