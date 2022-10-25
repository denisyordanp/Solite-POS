package com.socialite.solite_pos.data.source.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class SettingPref(context: Context) {

	companion object{
		private const val PREF_SETTING = "setting_preference"

		private const val PRINTER_DEVICE = "printer_device"
		private const val COOK_TIME = "cook_time"
	}

	private var preferences: SharedPreferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
	private var editor: SharedPreferences.Editor = preferences.edit()

	var printerDevice: String?
		get() {
			return preferences.getString(PRINTER_DEVICE, "")
		}
		set(value) {
			editor.putString(PRINTER_DEVICE, value)
			editor.apply()
		}

	var cookTime: Int
	get() {
		return preferences.getInt(COOK_TIME, 15)
	}
	set(value) {
		editor.putInt(COOK_TIME, value)
		editor.apply()
	}
}
