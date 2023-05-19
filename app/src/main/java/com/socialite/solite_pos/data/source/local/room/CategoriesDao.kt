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

    @Query("SELECT * FROM ${Category.DB_NAME} WHERE ${Category.ID} = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Long): Category?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(data: Category)

    @Update
    suspend fun updateCategory(data: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCategory(data: NewCategory)
}
