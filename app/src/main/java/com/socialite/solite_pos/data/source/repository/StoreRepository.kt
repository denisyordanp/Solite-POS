package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    fun getStores(): Flow<List<Store>>
    suspend fun getStore(id: String): Store?
    suspend fun insertStore(store: Store)
    suspend fun updateStore(store: Store)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldStores()
    suspend fun deleteAllNewStores()
}
