package com.socialite.data.schema.response

data class OrderDetailResponse(
    val amount: Int,
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val product: String
)
