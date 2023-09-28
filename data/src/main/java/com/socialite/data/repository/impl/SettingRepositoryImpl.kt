package com.socialite.data.repository.impl

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.socialite.common.di.IoDispatcher
import com.socialite.data.datastore.DataStoreManager
import com.socialite.data.preference.SettingPreferences
import com.socialite.data.preference.UserPreferences
import com.socialite.data.repository.SettingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val userPreference: UserPreferences,
    private val settingPreferences: SettingPreferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : SettingRepository {

    private object PreferencesKeys {
        val SELECTED_STORE = longPreferencesKey("selected_store")
        val NEW_SELECTED_STORE = stringPreferencesKey("new_selected_store")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_MIGRATE = booleanPreferencesKey("is_migrate")
        val IS_MIGRATE_PHASE_2 = booleanPreferencesKey("is_migrate_phase_2")
        val LAST_FORGOT_PASSWORD_SENT_TIME = longPreferencesKey("LAST_FORGOT_PASSWORD_SENT_TIME")
    }

    override fun getSelectedStore() = dataStoreManager.getData(PreferencesKeys.SELECTED_STORE, 0L)
        .flowOn(dispatcher)

    override fun getNewSelectedStore() =
        dataStoreManager.getData(PreferencesKeys.NEW_SELECTED_STORE, "")
            .flowOn(dispatcher)

    override fun getIsDarkModeActive() =
        dataStoreManager.getData(PreferencesKeys.IS_DARK_MODE, false)
            .flowOn(dispatcher)

    override fun selectNewStore(storeId: String) =
        dataStoreManager.saveData(PreferencesKeys.NEW_SELECTED_STORE, storeId).flowOn(dispatcher)

    override suspend fun setDarkMode(isActive: Boolean) {
        dataStoreManager.saveData(PreferencesKeys.IS_DARK_MODE, isActive)
            .collect()
    }

    override suspend fun isMigrated() = dataStoreManager.getData(PreferencesKeys.IS_MIGRATE, false)
        .first()

    override suspend fun isMigratedPhase2() =
        dataStoreManager.getData(PreferencesKeys.IS_MIGRATE_PHASE_2, false)
            .first()

    override suspend fun setMigration(isMigrate: Boolean) {
        dataStoreManager.saveData(PreferencesKeys.IS_MIGRATE, isMigrate)
            .collect()
    }

    override suspend fun setMigrationPhase2(isMigrate: Boolean) {
        dataStoreManager.saveData(PreferencesKeys.IS_MIGRATE_PHASE_2, isMigrate)
            .collect()
    }

    override fun insertToken(token: String) {
        userPreference.setUserToken(token)
    }

    override fun getToken(): String = userPreference.getUserToken()
    override fun getPrinterDeviceAddress(): String? {
        return settingPreferences.printerDevice
    }

    override fun setPrinterDeviceAddress(address: String) {
        settingPreferences.printerDevice = address
    }

    override fun setOrderCount(count: Int) {
        settingPreferences.orderCount = count
    }

    override fun getOrderCount(): Int {
        return settingPreferences.orderCount
    }

    override fun setLastOrderDate(date: String) {
        settingPreferences.orderDate = date
    }

    override fun getLastOrderDate(): String {
        return settingPreferences.orderDate
    }

    override fun getLastForgotPasswordTime() =
        dataStoreManager.getData(PreferencesKeys.LAST_FORGOT_PASSWORD_SENT_TIME, 0L)
            .flowOn(dispatcher)

    override fun setLastForgotPasswordTime(time: Long) =
        dataStoreManager.saveData(PreferencesKeys.LAST_FORGOT_PASSWORD_SENT_TIME, time)
            .flowOn(dispatcher)
}
