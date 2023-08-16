package com.socialite.data.preference

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@SuppressLint("CommitPrefEdits")
class SettingPreferences @Inject constructor(
	@ApplicationContext context: Context
) {

	companion object{
		private const val PRINTER_DEVICE = "printer_device"
		private const val PURCHASE_DATE = "purchase_date"
		private const val PURCHASE_ID = "purchase_id"
		private const val ORDER_DATE = "order_date"
		private const val ORDER_ID = "order_id"
	}

	private var preferences: SharedPreferences = PreferencesFactory(context).getPreferences(this::class.java)
	private var editor: SharedPreferences.Editor = preferences.edit()

	var printerDevice: String?
		get() {
			return preferences.getString(PRINTER_DEVICE, "")
		}
		set(value) {
			editor.putString(PRINTER_DEVICE, value)
			editor.apply()
		}

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
