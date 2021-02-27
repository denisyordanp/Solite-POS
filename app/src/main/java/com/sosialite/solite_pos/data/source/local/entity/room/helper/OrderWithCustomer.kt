package com.sosialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.sosialite.solite_pos.data.source.local.entity.room.master.Customer
import com.sosialite.solite_pos.data.source.local.entity.room.master.Order
import java.io.Serializable

data class OrderWithCustomer(
	@Embedded
	var order: Order,

	@Relation(parentColumn = Customer.ID, entityColumn = Customer.ID)
	var customer: Customer,
): Serializable
