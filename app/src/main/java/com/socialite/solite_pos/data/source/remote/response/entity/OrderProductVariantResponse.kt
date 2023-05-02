package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderProductVariantResponse(
    val id: String,
    val isUploaded: Boolean,
    val orderDetail: String,
    val variantOption: Int
)
