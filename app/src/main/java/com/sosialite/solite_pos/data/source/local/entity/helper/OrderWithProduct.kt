package com.sosialite.solite_pos.data.source.local.entity.helper

import com.sosialite.solite_pos.data.source.local.entity.room.helper.OrderWithPayment
import com.sosialite.solite_pos.data.source.local.entity.room.master.Customer
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import java.io.Serializable

data class OrderWithProduct(
	var order: Order,
	var payment: OrderWithPayment?,
	var customer: Customer,
	var products: ArrayList<ProductOrderDetail> = ArrayList()
): Serializable{
	constructor(order: Order, customer: Customer): this(order, null, customer, ArrayList())
	constructor(order: Order, payment: OrderWithPayment?, customer: Customer): this(order, payment, customer, ArrayList())
	constructor(order: Order, customer: Customer, products: ArrayList<ProductOrderDetail>): this(order, null, customer, products)

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
