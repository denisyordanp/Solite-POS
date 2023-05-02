package com.socialite.solite_pos.data.source.preference.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.socialite.solite_pos.data.source.preference.UserPreferences
import kotlinx.coroutines.flow.first

private const val USER_DATASTORE_PREFERENCES =
    "user_data_store_preferences"
private val Context.userDataStorePreferences by preferencesDataStore(
    USER_DATASTORE_PREFERENCES
)

class UserPreferencesImpl(
    private val dataStore: DataStore<Preferences>,
) : UserPreferences {

    companion object {
        fun getDataStoreInstance(context: Context): UserPreferencesImpl {
            val dataStore = context.userDataStorePreferences
            return UserPreferencesImpl(dataStore)
        }
    }

    private object PreferencesKeys {
        val USER_TOKEN = stringPreferencesKey("user_token_preference")
    }

    override suspend fun setUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = token
        }
    }

    override suspend fun getUserToken(): String? =
        dataStore.data.first()[PreferencesKeys.USER_TOKEN]
}
