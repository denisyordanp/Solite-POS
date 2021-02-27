package com.sosialite.solite_pos.data.source.remote.response.entity

import com.sosialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.sosialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.sosialite.solite_pos.data.source.local.entity.room.master.Supplier

data class PurchaseResponse(
        var suppliers: List<Supplier>,
        var purchases: List<Purchase>,
        var products: DataProductResponse,
        var purchaseProducts: List<PurchaseProduct>
){
    constructor(): this(emptyList(), emptyList(), DataProductResponse(), emptyList())
}