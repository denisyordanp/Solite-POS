package com.denisyordanp.data.schema

data class VariantProductResponse(
    val id: String,
    val isUploaded: Boolean,
    val product: String,
    val variant: String,
    val variantOption: String
)
