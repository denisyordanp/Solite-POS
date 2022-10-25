package com.socialite.solite_pos.data.source.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class PurchasePref(context: Context) {

    companion object {
        private const val PREF_SETTING = "setting_preference"

        private const val PURCHASE_DATE = "purchase_date"
        private const val PURCHASE_ID = "purchase_id"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    var purchaseCount: Int
        get() {
            return preferences.getInt(PURCHASE_ID, 1)
        }
        set(value) {
            editor.putInt(PURCHASE_ID, value)
            editor.apply()
        }

    var purchaseDate: String?
        get() {
            return preferences.getString(PURCHASE_DATE, "")
        }
        set(value) {
            editor.putString(PURCHASE_DATE, value)
            editor.apply()
        }
}
