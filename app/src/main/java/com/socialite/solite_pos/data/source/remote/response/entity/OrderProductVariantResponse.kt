package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant

data class OrderProductVariantResponse(
    val id: String,
    val isUploaded: Boolean,
    val orderDetail: String,
    val variantOption: String
) {
    fun toEntity(): OrderProductVariant {
        return OrderProductVariant(
            id = id,
            orderDetail = orderDetail,
            variantOption = variantOption,
            isUpload = isUploaded
        )
    }
}
