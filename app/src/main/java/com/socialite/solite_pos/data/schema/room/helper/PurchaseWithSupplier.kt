package com.socialite.solite_pos.data.schema.room.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.solite_pos.data.schema.room.master.Purchase
import com.socialite.solite_pos.data.schema.room.master.Supplier

data class PurchaseWithSupplier(
    @Embedded
	var purchase: Purchase,

    @Relation(parentColumn = Supplier.ID, entityColumn = Supplier.ID)
	var supplier: Supplier,
)
