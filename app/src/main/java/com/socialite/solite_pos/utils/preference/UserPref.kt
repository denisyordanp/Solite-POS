package com.socialite.solite_pos.utils.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class UserPref(context: Context) {

    companion object {
        private const val PREF_USER = "user_preference"

        private const val AUTHORITY = "authority"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    var userAuthority: String?
        get() {
            return preferences.getString(AUTHORITY, "")
        }
        set(value) {
            editor.putString(AUTHORITY, value)
            editor.apply()
        }
}
