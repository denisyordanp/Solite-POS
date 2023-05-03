package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderDetailResponse(
    val amount: Int,
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val product: String
)
