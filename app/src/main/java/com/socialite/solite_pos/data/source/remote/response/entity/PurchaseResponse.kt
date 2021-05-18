package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Purchase
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier

data class PurchaseResponse(
        var suppliers: List<Supplier>,
        var purchases: List<Purchase>
){
    constructor(): this(emptyList(), emptyList())
}