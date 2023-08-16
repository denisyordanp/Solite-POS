package com.socialite.solite_pos.data.schema.response

import com.socialite.data.schema.response.OrderResponse as DataOrderResponse
import com.socialite.solite_pos.data.schema.room.new_master.Order

data class OrderResponse(
        val id: String,
        val customer: String,
        val isTakeAway: Boolean,
        val isUploaded: Boolean,
        val orderNo: String,
        val orderTime: String,
        val status: Int,
        val store: String
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
                isUploaded = isUploaded
        )
    }
}
