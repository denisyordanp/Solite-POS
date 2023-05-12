package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant

data class OrderProductVariantResponse(
    val id: String,
    val isUploaded: Boolean,
    val orderDetail: String,
    val variantOption: String
) {
    fun toEntity(): OrderProductVariant {
        return OrderProductVariant(
            id = id.toLong(),
            idOrderDetail = orderDetail.toLong(),
            idVariantOption = variantOption.toLong(),
            isUpload = isUploaded
        )
    }
}
