package com.socialite.data.schema.response

data class OrderProductVariantResponse(
    val id: String,
    val isUploaded: Boolean,
    val orderDetail: String,
    val variantOption: String
)
