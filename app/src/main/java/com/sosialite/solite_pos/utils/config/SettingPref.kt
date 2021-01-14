package com.sosialite.solite_pos.utils.config

import android.content.Context

class SettingPref(private var context: Context) {

	companion object{
		private const val PREF_SETTING = "setting_preference"

		private const val SAVED_DATE = "saved_date"
		private const val COOK_TIME = "cook_time"
		private const val ORDER_ID = "order_id"
	}

	var cookTime: Int
	get() {
		val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
		return preferences.getInt(COOK_TIME, 15)
	}
	set(value) {
		val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
		val editor = preferences.edit()
		editor.putInt(COOK_TIME, value)
		editor.apply()
	}

	var orderCount: Int
		get() {
			val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
			return preferences.getInt(ORDER_ID, 1)
		}
		set(value) {
			val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
			val editor = preferences.edit()
			editor.putInt(ORDER_ID, value)
			editor.apply()
		}

	var savedDate: String?
		get() {
			val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
			return preferences.getString(SAVED_DATE, "")
		}
		set(value) {
			val preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
			val editor = preferences.edit()
			editor.putString(SAVED_DATE, value)
			editor.apply()
		}
}
