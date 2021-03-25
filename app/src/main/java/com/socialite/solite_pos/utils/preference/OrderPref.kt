package com.socialite.solite_pos.utils.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class OrderPref(context: Context) {

    companion object {
        private const val PREF_SETTING = "setting_preference"

        private const val ORDER_DATE = "order_date"
        private const val ORDER_ID = "order_id"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    var orderCount: Int
        get() {
            return preferences.getInt(ORDER_ID, 1)
        }
        set(value) {
            editor.putInt(ORDER_ID, value)
            editor.apply()
        }

    var orderDate: String?
        get() {
            return preferences.getString(ORDER_DATE, "")
        }
        set(value) {
            editor.putString(ORDER_DATE, value)
            editor.apply()
        }
}
