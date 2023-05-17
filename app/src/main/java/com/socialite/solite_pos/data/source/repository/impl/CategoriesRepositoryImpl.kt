package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CategoriesDao
import com.socialite.solite_pos.data.source.repository.CategoriesRepository
import kotlinx.coroutines.flow.first
import java.util.UUID

class CategoriesRepositoryImpl(
    private val dao: CategoriesDao,
    private val db: AppDatabase
) : CategoriesRepository {

    companion object {
        @Volatile
        private var INSTANCE: CategoriesRepositoryImpl? = null

        fun getInstance(
            dao: CategoriesDao,
            db: AppDatabase
        ): CategoriesRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(CategoriesRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = CategoriesRepositoryImpl(dao = dao, db = db)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getCategories(query: SimpleSQLiteQuery) = dao.getCategories(query)

    override suspend fun insertCategory(data: Category) {
        dao.insertCategory(data)
    }

    override suspend fun updateCategory(data: Category) {
        dao.updateCategory(data)
    }

    override suspend fun migrateToUUID() {
        val categories = dao.getCategories(Category.getFilter(Category.ALL)).first()
        db.withTransaction {
            for (category in categories) {
                dao.updateCategory(category.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
