package com.sosialite.solite_pos.utils.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressLint("CommitPrefEdits")
class SettingPref(context: Context) {

	companion object{
		private const val PREF_SETTING = "setting_preference"

		private const val PRINTER_DEVICE = "printer_device"
		private const val SAVED_DATE = "saved_date"
		private const val COOK_TIME = "cook_time"
		private const val ORDER_ID = "order_id"
	}

	private var preferences: SharedPreferences
	private var editor: SharedPreferences.Editor

	init {
		preferences = context.getSharedPreferences(PREF_SETTING, Context.MODE_PRIVATE)
		editor = preferences.edit()
	}

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

	var orderCount: Int
		get() {
			return preferences.getInt(ORDER_ID, 1)
		}
		set(value) {
			editor.putInt(ORDER_ID, value)
			editor.apply()
		}

	var savedDate: String?
		get() {
			return preferences.getString(SAVED_DATE, "")
		}
		set(value) {
			editor.putString(SAVED_DATE, value)
			editor.apply()
		}
}
