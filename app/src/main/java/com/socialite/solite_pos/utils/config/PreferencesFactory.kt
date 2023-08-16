package com.socialite.solite_pos.utils.config

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.socialite.solite_pos.data.preference.SettingPreferences
import com.socialite.solite_pos.data.preference.impl.UserPreferencesImpl

class PreferencesFactory(
    private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    fun <T> getPreferences(modelClass: Class<T>): SharedPreferences {
        return when {
            modelClass.isAssignableFrom(UserPreferencesImpl::class.java) -> create(USER_PREFERENCES)
            modelClass.isAssignableFrom(SettingPreferences::class.java) -> create(
                SETTING_PREFERENCES
            )

            else -> throw IllegalArgumentException("No preferences for $modelClass")
        }
    }

    private fun create(name: String) = EncryptedSharedPreferences.create(
        context,
        name,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val USER_PREFERENCES = "user_preferences"
        private const val SETTING_PREFERENCES = "setting_preference"
    }
}
