package com.socialite.solite_pos.data.source.preference.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.socialite.solite_pos.data.source.preference.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferences {

    companion object {
        private const val USER_PREFERENCES = "user_preferences"
        private const val USER_TOKEN = "user_token"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val preferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        USER_PREFERENCES,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private var editor: SharedPreferences.Editor = preferences.edit()

    override fun setUserToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    override fun getUserToken(): String =
        preferences.getString(USER_TOKEN, "") ?: ""
}
