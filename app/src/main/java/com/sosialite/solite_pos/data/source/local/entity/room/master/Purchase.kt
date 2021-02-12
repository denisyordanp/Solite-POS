package com.sosialite.solite_pos.data.source.local.entity.room.master

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentDate
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentTime
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.SettingPref
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(
	tableName = AppDatabase.TBL_PURCHASE,
	primaryKeys = [Purchase.NO],
	indices = [
		Index(value = [Purchase.NO])
	]
)
data class Purchase(
	@ColumnInfo(name = NO)
	var purchaseNo: String,

	@ColumnInfo(name = Supplier.ID)
	var idSupplier: Int,

	@ColumnInfo(name = PURCHASE_DATE)
	var purchaseTime: String
): Serializable {

	companion object{

		const val PURCHASE_DATE = "purchase_date"
		const val NO = "purchase_no"

		private var setting: SettingPref? = null

		fun purchaseNo(context: Context): String{
			setting = SettingPref(context)
			return id
		}

		private val id: String
			get() {
				if (date != savedDate){
					saveDate()
				}
				return "$savedDate${setNumber(setting!!.purchaseCount)}"
			}

		private fun setNumber(i: Int): String{
			var str = i.toString()
			when(str.length){
				1 -> str = "00$i"
				2 -> str = "0$i"
			}
			return str
		}

		private val date: String
			get() {
				val fd = SimpleDateFormat("ddMMyy", Locale.getDefault())
				return fd.format(currentTime)
			}

		private val savedDate: String
			get() {
				val date = setting!!.purchaseDate
				return date ?: ""
			}

		private fun saveDate(){
			setting!!.purchaseDate = date
			reset()
		}

		fun add(context: Context){
			if (setting == null){
				setting = SettingPref(context)
			}
			setting!!.purchaseCount = setting!!.purchaseCount+1
		}

		private fun reset(){
			setting!!.purchaseCount = 1
		}
	}

	constructor(context: Context, idSupplier: Int): this(purchaseNo(context), idSupplier, currentDate)

	val timeString: String
		get() {
			return dateFormat(purchaseTime, MainConfig.ldFormat)
		}

}
