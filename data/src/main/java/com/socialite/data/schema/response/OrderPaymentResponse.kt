package com.socialite.data.schema.response

import com.socialite.schema.database.new_bridge.OrderPayment

data class OrderPaymentResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val pay: Int,
    val payment: String
) {
    fun toEntity(): OrderPayment {
        return OrderPayment(
            id = id,
            order = order,
            payment = payment,
            pay = pay.toLong(),
            isUpload = isUploaded
        )
    }
}
