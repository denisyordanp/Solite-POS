package com.socialite.solite_pos.data.source.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    fun getSelectedStore(): Flow<String>
    fun getIsDarkModeActive(): Flow<Boolean>
    suspend fun selectStore(storeId: String)
    suspend fun setDarkMode(isActive: Boolean)
    suspend fun isMigrated(): Boolean
    suspend fun setMigration(isMigrate: Boolean)
}
