package com.socialite.data.schema.response

import com.socialite.schema.database.new_bridge.OrderDetail

data class OrderDetailResponse(
    val amount: Int,
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val product: String
) {
    fun toEntity(): OrderDetail {
        return OrderDetail(
            id = id,
            order = order,
            product = product,
            amount = amount,
            isUpload = isUploaded,
            isDeleted = false
        )
    }
}

