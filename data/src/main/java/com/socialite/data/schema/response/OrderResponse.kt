package com.socialite.data.schema.response

import com.socialite.schema.database.new_master.Order

data class OrderResponse(
    val id: String,
    val customer: String,
    val isTakeAway: Boolean,
    val isUploaded: Boolean,
    val orderNo: String,
    val orderTime: String,
    val status: Int,
    val store: String,
    val user: Long
) {
    fun toEntity(): Order {
        return Order(
            id = id,
            orderNo = orderNo,
            customer = customer,
            orderTime = orderTime,
            isTakeAway = isTakeAway,
            status = status,
            store = store,
            isUploaded = isUploaded,
            user = user
        )
    }
}
