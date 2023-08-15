package com.socialite.solite_pos.data.schema.response

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo

data class OrderPromoResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val promo: String,
    val totalPromo: Int
) {
    fun toEntity(): OrderPromo {
        return OrderPromo(
            id = id,
            order = order,
            promo = promo,
            totalPromo = totalPromo.toLong(),
            isUpload = isUploaded
        )
    }
}
