package com.socialite.solite_pos.data.schema.response

import com.socialite.data.schema.response.OrderDetailResponse as DataResponse
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderDetail

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
