package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderPaymentResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val pay: Int,
    val payment: String
)
