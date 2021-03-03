package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.bridge.*
import com.socialite.solite_pos.data.source.local.entity.room.master.Customer
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment

data class OrderProductResponse(
    var products: DataProductResponse,
    var details: List<OrderDetail>,
    var mixOrders: List<OrderProductVariantMix>,
    var mixVariants: List<OrderMixProductVariant>,
    var variants: List<OrderProductVariant>
){
    constructor(): this(DataProductResponse(), emptyList(), emptyList(), emptyList(), emptyList())
}