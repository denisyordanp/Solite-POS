package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import java.io.Serializable

data class OrderWithProduct(
		var order: OrderData,
		var products: List<ProductOrderDetail>
): Serializable {

	constructor(order: OrderData): this(order, emptyList())

	val grandTotal: Long
		get() {
			var total = 0L
			for (item in products){
				if (item.product != null){
					total += item.product!!.sellPrice * item.amount
				}
			}
			return total
		}

	val totalItem: Int
	get() {
		var total = 0
		for (item in products){
			total += item.amount
		}
		return total
	}
}
