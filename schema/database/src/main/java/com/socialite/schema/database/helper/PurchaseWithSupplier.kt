package com.socialite.schema.database.helper

import androidx.room.Embedded
import androidx.room.Relation
import com.socialite.schema.database.master.Purchase
import com.socialite.schema.database.master.Supplier

data class PurchaseWithSupplier(
    @Embedded
	var purchase: Purchase,

    @Relation(parentColumn = Supplier.ID, entityColumn = Supplier.ID)
	var supplier: Supplier,
)
