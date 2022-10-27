package com.socialite.solite_pos.data.source.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getSelectedStore(): Flow<Long>
    fun getIsDarkModeActive(): Flow<Boolean>
    suspend fun selectStore(storeId: Long)
    suspend fun setDarkMode(isActive: Boolean)
}
