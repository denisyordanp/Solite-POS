package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail

data class OrderDetailResponse(
    val amount: Int,
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val product: String
) {
    fun toEntity(): OrderDetail {
        return OrderDetail(
            id = id.toLong(),
            orderNo = order,
            idProduct = product.toLong(),
            amount = amount,
            isUpload = isUploaded
        )
    }
}
