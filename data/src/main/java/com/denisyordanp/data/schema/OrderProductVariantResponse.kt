package com.denisyordanp.data.schema

data class OrderProductVariantResponse(
    val id: String,
    val isUploaded: Boolean,
    val orderDetail: String,
    val variantOption: String
)
