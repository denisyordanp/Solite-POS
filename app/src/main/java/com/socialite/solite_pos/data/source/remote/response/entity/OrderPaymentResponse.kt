package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderPaymentResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val pay: String,
    val payment: String
)
