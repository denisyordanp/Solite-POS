package com.sosialite.solite_pos.utils.config

import android.content.Context

class SettingPref(private var context: Context) {

	companion object{
		private const val PREF_SETTING = "setting_preference"

		private const val COOK_TIME = "cook_time"
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
}
