package com.socialite.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.socialite.data.database.AppDatabase
import com.socialite.data.schema.room.master.Store
import com.socialite.data.schema.room.new_master.Store as NewStore
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Query("SELECT * FROM ${Store.DB_NAME}")
    fun getStores(): Flow<List<Store>>

    @Query("SELECT * FROM ${NewStore.DB_NAME}")
    fun getNewStores(): Flow<List<NewStore>>

    @Query("SELECT * FROM ${NewStore.DB_NAME}  WHERE ${AppDatabase.UPLOAD} = 0")
    suspend fun getNeedUploadStores(): List<NewStore>

    @Query("SELECT * FROM ${Store.DB_NAME} WHERE ${Store.ID} = :id LIMIT 1")
    suspend fun getStore(id: Long): Store?

    @Query("SELECT * FROM ${NewStore.DB_NAME} WHERE ${NewStore.ID} = :id LIMIT 1")
    suspend fun getNewStore(id: String): NewStore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStore(store: NewStore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewStore(store: NewStore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(list: List<NewStore>)

    @Update
    suspend fun updateStore(store: Store)

    @Update
    suspend fun updateStores(store: List<NewStore>)

    @Update
    suspend fun updateNewStore(store: NewStore)

    @Query("DELETE FROM ${Store.DB_NAME}")
    suspend fun deleteAllOldStore()

    @Query("DELETE FROM ${NewStore.DB_NAME}")
    suspend fun deleteAllNewStore()
}
