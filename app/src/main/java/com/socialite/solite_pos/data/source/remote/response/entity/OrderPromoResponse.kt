package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo

data class OrderPromoResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val promo: String,
    val totalPromo: Int
) {
    fun toEntity(): OrderPromo {
        return OrderPromo(
            id = id.toLong(),
            orderNO = order,
            idPromo = promo.toLong(),
            totalPromo = totalPromo.toLong(),
            isUpload = isUploaded
        )
    }
}
