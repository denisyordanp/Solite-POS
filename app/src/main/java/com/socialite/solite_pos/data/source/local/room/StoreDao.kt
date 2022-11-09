package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * FROM ${Store.DB_NAME}")
    fun getStores(): Flow<List<Store>>

    @Query("SELECT * FROM ${Store.DB_NAME} WHERE ${Store.ID} = :id")
    suspend fun getStore(id: Long): Store?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: Store)

    @Update
    suspend fun updateStore(store: Store)
}
