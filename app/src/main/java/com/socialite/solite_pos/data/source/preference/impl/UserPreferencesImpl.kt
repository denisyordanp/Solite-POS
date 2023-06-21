package com.socialite.solite_pos.data.source.preference.impl

import android.content.Context
import android.content.SharedPreferences
import com.socialite.solite_pos.data.source.preference.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferences {

    companion object {
        fun getInstance(context: Context): UserPreferencesImpl {
            return UserPreferencesImpl(context)
        }

        private const val USER_PREFERENCES = "user_preferences"
        private const val USER_TOKEN = "user_token"
    }

    private var preferences: SharedPreferences =
        context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    override fun setUserToken(token: String) {
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    override fun getUserToken(): String =
        preferences.getString(USER_TOKEN, "") ?: ""
}
