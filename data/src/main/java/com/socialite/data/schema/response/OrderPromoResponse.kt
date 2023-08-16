package com.socialite.data.schema.response

import com.socialite.data.schema.room.new_bridge.OrderPromo

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
