package com.sosialite.solite_pos.data.source.local.entity.room.master

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sosialite.solite_pos.data.source.local.room.AppDatabase
import com.sosialite.solite_pos.utils.config.MainConfig
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.currentTime
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.dateFormat
import com.sosialite.solite_pos.utils.config.MainConfig.Companion.strToDate
import com.sosialite.solite_pos.utils.config.SettingPref
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(
	tableName = AppDatabase.TBL_ORDER,
	primaryKeys = [Order.NO],
	foreignKeys = [
		ForeignKey(
			entity = Customer::class,
			parentColumns = [Customer.ID],
			childColumns = [Customer.ID],
			onDelete = ForeignKey.CASCADE)
	],
	indices = [
		Index(value = [Customer.ID])
	]
)
data class Order(
	@ColumnInfo(name = NO)
	var orderNo: String,

	@ColumnInfo(name = Customer.ID)
	var customer: Int,

	@ColumnInfo(name = ORDER_DATE)
	var orderTime: String,

	@ColumnInfo(name = COOK_TIME)
	var cookTime: String?,

	@ColumnInfo(name = TAKE_AWAY)
	var isTakeAway: Boolean,

	@ColumnInfo(name = STATUS)
	var status: Int
): Serializable {

	companion object{

		const val ORDER_DATE = "order_date"
		const val COOK_TIME = "cook_time"
		const val TAKE_AWAY = "take_away"
		const val STATUS = "status"
		const val NO = "order_no"

		const val ON_PROCESS = 0
		const val NEED_PAY = 1
		const val CANCEL = 2
		const val DONE = 3

		fun getFilter(status: Int): SimpleSQLiteQuery {
			val query = StringBuilder().append("SELECT * FROM ")
			query.append(AppDatabase.TBL_ORDER)
			query.append(" WHERE ")
			query.append(STATUS)
			query.append(" = ").append(status)
			return SimpleSQLiteQuery(query.toString())
		}

		private var setting: SettingPref? = null

		fun orderNo(context: Context): String{
			setting = SettingPref(context)
			return id
		}

		private val id: String
			get() {
				if (date != savedDate){
					saveDate()
				}
				return "$savedDate${setNumber(setting!!.orderCount)}"
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
				val date = setting!!.savedDate
				return date ?: ""
			}

		private fun saveDate(){
			setting!!.savedDate = date
			reset()
		}

		fun add(context: Context){
			if (setting == null){
				setting = SettingPref(context)
			}
			setting!!.orderCount = setting!!.orderCount+1
		}

		private fun reset(){
			setting!!.orderCount = 1
		}
	}

	constructor(orderNo: String, customer: Int, orderTime: String): this(orderNo, customer, orderTime, null, false, ON_PROCESS)

	fun isCancelable(context: Context): Boolean{
		return if (cookTime != null){
			currentTime.before(getFinishCook(context).time)
		}else{
			true
		}
	}

	val timeString: String
		get() {
			return dateFormat(orderTime, MainConfig.ldFormat)
		}

	fun getFinishCook(context: Context): Calendar{
		return if (cookTime != null){
			val finish: Calendar = Calendar.getInstance()

			finish.time = strToDate(cookTime)
			finish.add(Calendar.MINUTE, SettingPref(context).cookTime)
			finish
		}else{
			Calendar.getInstance()
		}
	}

	fun finishToString(context: Context): String?{
		return if (cookTime != null){
			val df = SimpleDateFormat("HH:mm", Locale.getDefault())

			val f = getFinishCook(context)
			f.add(Calendar.MINUTE, SettingPref(context).cookTime)
			df.format(f.time)
		}else{
			null
		}
	}
}
