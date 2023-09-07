package com.socialite.data.schema.helper

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.socialite.data.schema.room.master.Product
import com.socialite.data.schema.room.master.PurchaseProduct

data class PurchaseProductWithProduct(
    @Embedded
    val purchaseProduct: PurchaseProduct?,

    @Relation(parentColumn = Product.ID, entityColumn = Product.ID)
    val product: Product?,

    @Ignore
    val type: Int?
)
