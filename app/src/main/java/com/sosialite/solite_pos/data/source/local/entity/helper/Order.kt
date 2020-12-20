package com.sosialite.solite_pos.data.source.local.entity.helper

import java.util.*
import kotlin.collections.ArrayList

data class Order(
	var name: String,
	var orderNo: String,
	var items: ArrayList<DetailOrder>,
	var cookTime: Calendar
){

	companion object{
		const val COOK_TIME = 13
	}
	val totalPay: Int
	get() {
		var total = 0
		for (order in items){
			total += order.product.price * order.amount
		}
		return total
	}

	val totalItem: Int
	get() {
		var total = 0
		for (order in items){
			total += order.amount
		}
		return total
	}

	val strFinishCook: String
	get() {
		val finish = cookTime
		finish.add(Calendar.MINUTE, COOK_TIME)
		return "${finish[Calendar.HOUR_OF_DAY]}:${finish[Calendar.MINUTE]}"
	}
}
