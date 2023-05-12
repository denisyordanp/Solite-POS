package com.socialite.solite_pos.data.source.remote.response.entity

import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment

data class OrderPaymentResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val pay: Int,
    val payment: String
) {
    fun toEntity(): OrderPayment {
        return OrderPayment(
            id = id.toLong(),
            orderNO = order,
            idPayment = payment.toLong(),
            pay = pay.toLong(),
            isUpload = isUploaded
        )
    }
}
