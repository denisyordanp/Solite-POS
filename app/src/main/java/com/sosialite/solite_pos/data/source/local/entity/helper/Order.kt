package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.Customer
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Order(
	var customer: Customer?,
	var orderNo: String,
	var items: ArrayList<DetailOrder>,
	var orderTime: Date,
	var cookTime: Calendar?,
	var pay: Int,
	var status: Int
): Serializable{

	companion object{
		const val COOK_TIME = 13

		const val ON_PROCESS = 0
		const val NEED_PAY = 1
		const val DONE = 2
	}

	constructor(orderNo: String, orderTime: Date): this(null, orderNo, ArrayList(), orderTime, null, 0, ON_PROCESS)

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

	val strFinishCook: String?
	get() {
		return if (cookTime != null){
			val finish = cookTime
			finish!!.add(Calendar.MINUTE, COOK_TIME)
			"${finish[Calendar.HOUR_OF_DAY]}:${finish[Calendar.MINUTE]}"
		}else{
			null
		}
	}
}
