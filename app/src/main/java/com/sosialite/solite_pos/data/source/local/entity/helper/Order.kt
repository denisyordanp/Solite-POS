package com.sosialite.solite_pos.data.source.local.entity.helper

import android.content.Context
import com.sosialite.solite_pos.data.source.local.entity.main.Customer
import com.sosialite.solite_pos.utils.config.SettingPref
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Order(
        var customer: Customer?,
        var orderNo: Int,
        var items: ArrayList<DetailOrder>,
        var orderTime: Date,
        var cookTime: Calendar?,
        var pay: Int,
        var status: Int
): Serializable{

	companion object{
		const val ON_PROCESS = 0
		const val NEED_PAY = 1
		const val DONE = 2

	}

	constructor(customer: Customer, orderNo: Int, orderTime: Date): this(customer, orderNo, ArrayList(), orderTime, null, 0, ON_PROCESS)

	val timeString: String
	get() = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(orderTime)

	val totalPay: Int
	get() {
		var total = 0
		for (order in items){
			if (order.product != null){
				total += order.product!!.price * order.amount
			}
		}
		return total
	}

	val payReturn: Int
	get() {
		return pay - totalPay
	}

	val totalItem: Int
	get() {
		return items.size
	}

	fun getFinishCook(context: Context): Calendar?{
		return if (cookTime != null){
			val finish: Calendar = Calendar.getInstance()

			finish.time = cookTime!!.time
			finish.add(Calendar.SECOND, SettingPref(context).cookTime)
			finish
		}else{
			null
		}
	}

	fun finishToString(context: Context): String?{
		return if (cookTime != null){
			val df = SimpleDateFormat("HH:mm", Locale.getDefault())
			val finish: Calendar = Calendar.getInstance()

			finish.time = cookTime!!.time
			finish.add(Calendar.MINUTE, SettingPref(context).cookTime)
			df.format(finish.time)
		}else{
			null
		}
	}
}
