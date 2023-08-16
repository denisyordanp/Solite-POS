package com.socialite.data.schema.response

data class OrderResponse(
        val id: String,
        val customer: String,
        val isTakeAway: Boolean,
        val isUploaded: Boolean,
        val orderNo: String,
        val orderTime: String,
        val status: Int,
        val store: String
)
