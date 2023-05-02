package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderResponse(
    val cookTime: String,
    val customer: Int,
    val isTakeAway: Boolean,
    val isUploaded: Boolean,
    val orderNo: String,
    val orderTime: String,
    val status: Int,
    val store: Int
)
