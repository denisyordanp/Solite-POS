package com.socialite.data.repository

import com.socialite.schema.database.new_master.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository : SyncRepository<Store> {
    suspend fun getStore(id: String): Store?
    fun getStores(): Flow<List<Store>>
    suspend fun getNeedUploadStores(): List<Store>
    suspend fun insertStore(store: Store)
    suspend fun updateStore(store: Store)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldStores()
    suspend fun deleteAllNewStores()
}
