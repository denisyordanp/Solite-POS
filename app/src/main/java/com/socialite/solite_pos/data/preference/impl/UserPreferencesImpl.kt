package com.socialite.solite_pos.data.preference.impl

import android.content.Context
import android.content.SharedPreferences
import com.socialite.solite_pos.data.preference.UserPreferences
import com.socialite.solite_pos.utils.config.PreferencesFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferences {

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    private val preferences = PreferencesFactory(context).getPreferences(this::class.java)
    private val editor: SharedPreferences.Editor = preferences.edit()

    override fun setUserToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    override fun getUserToken(): String =
        preferences.getString(USER_TOKEN, "") ?: ""
}
