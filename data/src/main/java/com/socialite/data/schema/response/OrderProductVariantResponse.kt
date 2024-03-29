package com.socialite.data.schema.response

import com.socialite.data.schema.room.new_bridge.OrderProductVariant

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
            isUpload = isUploaded,
            isDeleted = false
        )
    }
}
