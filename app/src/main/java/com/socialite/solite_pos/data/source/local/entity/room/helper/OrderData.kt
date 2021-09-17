package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import java.io.Serializable

data class OrderData(
		@Embedded
		var order: Order,

		@Relation(parentColumn = Customer.ID, entityColumn = Customer.ID)
		var customer: Customer,

		@Relation(parentColumn = Order.NO, entityColumn = Order.NO)
		var orderPayment: OrderPayment?,

		@Relation(
				parentColumn = Order.NO,
				entity = Payment::class,
				entityColumn = Payment.ID,
				associateBy = Junction(
						value = OrderPayment::class,
						parentColumn = Order.NO,
						entityColumn = Payment.ID
				)
		) val payment: Payment?
): Serializable {
	@Ignore
	constructor(order: Order, customer: Customer): this(order, customer, null, null)
}
