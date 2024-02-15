package com.socialite.data.repository

import com.socialite.schema.database.EntityData

interface SyncRepository<T : EntityData> {
    suspend fun getItems(): List<T>
    suspend fun updateItems(items: List<T>)
    suspend fun insertItems(items: List<T>)
    suspend fun updateSynchronization(missingItems: List<T>?)
}
