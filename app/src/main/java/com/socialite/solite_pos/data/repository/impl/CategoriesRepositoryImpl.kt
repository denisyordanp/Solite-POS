package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.master.Category
import com.socialite.solite_pos.data.schema.room.new_master.Category as NewCategory
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CategoriesDao
import com.socialite.solite_pos.data.repository.CategoriesRepository
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
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

    override fun getCategories(query: SimpleSQLiteQuery) = dao.getNewCategories(query)
    override suspend fun getNeedUploadCategories() = dao.getNeedUploadCategories()
    override suspend fun insertCategory(data: NewCategory) {
        dao.insertNewCategory(data)
    }
    override suspend fun updateCategory(data: NewCategory) {
        dao.updateNewCategory(data.copy(
            isUploaded = false
        ))
    }

    override suspend fun migrateToUUID() {
        val categories = dao.getCategories(Category.getFilter(Category.ALL)).firstOrNull()
        if (!categories.isNullOrEmpty()) {
            db.withTransaction {
                for (category in categories) {
                    val uuid = category.new_id.ifEmpty {
                        val updatedCategory = category.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        dao.updateCategory(updatedCategory)
                        updatedCategory.new_id
                    }

                    val newCategory = NewCategory(
                        id = uuid,
                        name = category.name,
                        desc = category.desc,
                        isActive = category.isActive,
                        isUploaded = category.isUploaded
                    )
                    dao.insertNewCategory(newCategory)
                }
            }
        }
    }

    override suspend fun deleteAllOldCategories() {
        dao.deleteAllOldCategories()
    }

    override suspend fun deleteAllNewCategories() {
        dao.deleteAllNewCategories()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<NewCategory>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun getItems() = dao.getNewCategories(NewCategory.getFilter(NewCategory.ALL)).first()

    override suspend fun insertItems(items: List<NewCategory>) {
        dao.insertCategories(items)
    }

    override suspend fun updateItems(items: List<NewCategory>) {
        dao.updateCategories(items)
    }
}
