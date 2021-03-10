package com.socialite.solite_pos.data.source.local.entity.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.solite_pos.data.source.local.entity.room.master.Category
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier

data class PurchaseWithSupplier(
	@Embedded
	var purchase: Purchase,

	@Relation(parentColumn = Supplier.ID, entityColumn = Supplier.ID)
	var supplier: Supplier,
)
