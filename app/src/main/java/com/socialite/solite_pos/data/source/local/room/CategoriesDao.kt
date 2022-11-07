package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @RawQuery(observedEntities = [Category::class])
    fun getCategories(query: SupportSQLiteQuery): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(data: Category)

    @Update
    suspend fun updateCategory(data: Category)
}
