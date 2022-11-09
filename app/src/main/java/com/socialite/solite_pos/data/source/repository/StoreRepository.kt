package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.master.Store
import kotlinx.coroutines.flow.Flow

interface StoreRepository {

    fun getStores(): Flow<List<Store>>
    suspend fun getStore(id: Long): Store?
    suspend fun insertStore(store: Store)
    suspend fun updateStore(store: Store)
}
