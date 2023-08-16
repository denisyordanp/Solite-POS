package com.socialite.data.schema.response

data class OrderPaymentResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val pay: Int,
    val payment: String
)
