package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.PurchaseProduct

data class PurchaseProductResponse(
        var purchases: List<PurchaseProduct>,
        var products: DataProductResponse
){
    constructor(): this(emptyList(), DataProductResponse())
}