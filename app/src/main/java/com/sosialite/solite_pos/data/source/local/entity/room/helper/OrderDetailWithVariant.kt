package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import java.io.Serializable

data class OrderDetailWithVariant(
		@Embedded
		var order: Order? = null,

		@Relation(parentColumn = Order.NO, entityColumn = Order.NO)
		var details: List<OrderDetail> = emptyList()
): Serializable
