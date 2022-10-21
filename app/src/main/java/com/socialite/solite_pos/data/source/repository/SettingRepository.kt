package com.socialite.solite_pos.data.source.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getSelectedStore(): Flow<Long>
    suspend fun selectStore(storeId: Long)
}
