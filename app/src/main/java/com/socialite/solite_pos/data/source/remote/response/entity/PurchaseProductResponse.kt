package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier

data class PurchaseProductResponse(
        var purchases: List<PurchaseProduct>,
        var products: DataProductResponse
){
    constructor(): this(emptyList(), DataProductResponse())
}