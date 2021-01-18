package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.VariantProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.*
import java.io.Serializable

data class OrderWithPayment(
		@Embedded
		var order: Order,

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
): Serializable
