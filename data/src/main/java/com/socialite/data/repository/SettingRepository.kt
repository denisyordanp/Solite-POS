package com.socialite.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingRepository {
    fun getSelectedStore(): Flow<Long>
    fun getNewSelectedStore(): Flow<String>
    fun getIsDarkModeActive(): Flow<Boolean>
    fun selectNewStore(storeId: String): Flow<Boolean>
    suspend fun setDarkMode(isActive: Boolean)
    suspend fun isMigrated(): Boolean
    suspend fun isMigratedPhase2(): Boolean
    suspend fun setMigration(isMigrate: Boolean)
    suspend fun setMigrationPhase2(isMigrate: Boolean)
    fun insertToken(token: String)
    fun getToken(): String

    fun getPrinterDeviceAddress(): String?
    fun setPrinterDeviceAddress(address: String)

    fun setOrderCount(count: Int)
    fun getOrderCount(): Int

    fun setLastOrderDate(date: String)
    fun getLastOrderDate(): String
}
