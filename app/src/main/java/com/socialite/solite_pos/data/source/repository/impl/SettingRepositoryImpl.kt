package com.socialite.solite_pos.data.source.repository.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.socialite.solite_pos.data.source.preference.UserPreferences
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.di.DataStoreModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val userPreference: UserPreferences
) : SettingRepository {
    companion object {
        fun getDataStoreInstance(
            context: Context,
            userPreference: UserPreferences
        ): SettingRepositoryImpl {
            val dataStore = DataStoreModule.provideSettingDataStore(context)
            return SettingRepositoryImpl(dataStore, userPreference)
        }
    }

    private object PreferencesKeys {
        val SELECTED_STORE = longPreferencesKey("selected_store")
        val NEW_SELECTED_STORE = stringPreferencesKey("new_selected_store")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_MIGRATE = booleanPreferencesKey("is_migrate")
        val IS_MIGRATE_PHASE_2 = booleanPreferencesKey("is_migrate_phase_2")
    }

    override fun getSelectedStore() = dataStore.data.map {
        it[PreferencesKeys.SELECTED_STORE] ?: 0L
    }

    override fun getNewSelectedStore() = dataStore.data.map {
        it[PreferencesKeys.NEW_SELECTED_STORE] ?: ""
    }

    override fun getIsDarkModeActive() = dataStore.data.map {
        it[PreferencesKeys.IS_DARK_MODE] ?: false
    }

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
}
