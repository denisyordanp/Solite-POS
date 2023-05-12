package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.master.Order

data class OrderResponse(
    val cookTime: String?,
    val customer: Int,
    val isTakeAway: Boolean,
    val isUploaded: Boolean,
    val orderNo: String,
    val orderTime: String,
    val status: Int,
    val store: Int
) {
    fun toEntity(): Order {
        return Order(
            orderNo = orderNo,
            customer = customer.toLong(),
            orderTime = orderTime,
            cookTime = cookTime,
            isTakeAway = isTakeAway,
            status = status,
            store = store.toLong(),
            isUploaded = isUploaded
        )
    }
}
