package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.helper.EntityData

interface SyncRepository<T : EntityData> {
    suspend fun getItems(): List<T>
    suspend fun updateItems(items: List<T>)
    suspend fun insertItems(items: List<T>)
    suspend fun updateSynchronization(missingItems: List<T>?)
}