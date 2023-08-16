package com.socialite.solite_pos.data.repository

import com.socialite.solite_pos.data.schema.room.EntityData

interface SyncRepository<T : EntityData> {
    suspend fun getItems(): List<T>
    suspend fun updateItems(items: List<T>)
    suspend fun insertItems(items: List<T>)
    suspend fun updateSynchronization(missingItems: List<T>?)
}
