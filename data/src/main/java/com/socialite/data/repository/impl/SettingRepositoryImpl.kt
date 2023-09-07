package com.socialite.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.socialite.common.di.IoDispatcher
import com.socialite.data.preference.SettingPreferences
import com.socialite.data.preference.UserPreferences
import com.socialite.data.repository.SettingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
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
    }

    override fun getSelectedStore() = dataStore.data.map {
        it[PreferencesKeys.SELECTED_STORE] ?: 0L
    }.flowOn(dispatcher)

    override fun getNewSelectedStore() = dataStore.data.map {
        it[PreferencesKeys.NEW_SELECTED_STORE] ?: ""
    }.flowOn(dispatcher)

    override fun getIsDarkModeActive() = dataStore.data.map {
        it[PreferencesKeys.IS_DARK_MODE] ?: false
    }.flowOn(dispatcher)

    override suspend fun selectNewStore(storeId: String) {
        dataStore.edit {
            it[PreferencesKeys.NEW_SELECTED_STORE] = storeId
        }
    }

    override suspend fun setDarkMode(isActive: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_DARK_MODE] = isActive
        }
    }

    override suspend fun isMigrated() = dataStore.data.map {
        it[PreferencesKeys.IS_MIGRATE] ?: false
    }.first()

    override suspend fun isMigratedPhase2() = dataStore.data.map {
        it[PreferencesKeys.IS_MIGRATE_PHASE_2] ?: false
    }.first()

    override suspend fun setMigration(isMigrate: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_MIGRATE] = isMigrate
        }
    }

    override suspend fun setMigrationPhase2(isMigrate: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_MIGRATE_PHASE_2] = isMigrate
        }
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
}
