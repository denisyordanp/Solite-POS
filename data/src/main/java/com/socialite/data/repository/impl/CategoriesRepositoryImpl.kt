package com.socialite.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.CategoriesDao
import com.socialite.data.repository.CategoriesRepository
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.master.Category
import com.socialite.data.schema.room.new_master.Category as NewCategory
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val dao: CategoriesDao,
    private val db: AppDatabase
) : CategoriesRepository {

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
