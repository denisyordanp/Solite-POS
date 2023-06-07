package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category as NewCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @RawQuery(observedEntities = [Category::class])
    fun getCategories(query: SupportSQLiteQuery): Flow<List<Category>>

    @RawQuery(observedEntities = [NewCategory::class])
    fun getNewCategories(query: SupportSQLiteQuery): Flow<List<NewCategory>>

    @Query("SELECT * FROM ${NewCategory.DB_NAME} WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadCategories(): List<NewCategory>

    @Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Long): Category?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(data: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(list: List<NewCategory>)

    @Update
    suspend fun updateCategory(data: Category)

    @Update
    suspend fun updateCategories(data: List<NewCategory>)

    @Update
    suspend fun updateNewCategory(data: NewCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCategory(data: NewCategory)

    @Query("DELETE FROM ${Category.DB_NAME}")
    suspend fun deleteAllOldCategories()

    @Query("DELETE FROM ${NewCategory.DB_NAME}")
    suspend fun deleteAllNewCategories()
}
