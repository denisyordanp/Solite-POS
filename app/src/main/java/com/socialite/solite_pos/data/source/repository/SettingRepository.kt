package com.socialite.solite_pos.data.source.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getSelectedStore(): Flow<Long>
    fun getNewSelectedStore(): Flow<String>
    fun getIsDarkModeActive(): Flow<Boolean>
    suspend fun selectNewStore(storeId: String)
    suspend fun selectStore(storeId: Long)
    suspend fun setDarkMode(isActive: Boolean)
    suspend fun isMigrated(): Boolean
    suspend fun isMigratedPhase2(): Boolean
    suspend fun setMigration(isMigrate: Boolean)
    suspend fun setMigrationPhase2(isMigrate: Boolean)
    fun insertToken(token: String)
    fun getToken(): String
}
