package com.socialite.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.socialite.data.schema.room.new_master.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository : SyncRepository<Category> {

    fun getCategories(query: SimpleSQLiteQuery): Flow<List<Category>>
    suspend fun getNeedUploadCategories(): List<Category>
    suspend fun insertCategory(data: Category)
    suspend fun updateCategory(data: Category)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldCategories()
    suspend fun deleteAllNewCategories()
}
