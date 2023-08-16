package com.socialite.data.schema.response

data class VariantProductResponse(
    val id: String,
    val isUploaded: Boolean,
    val product: String,
    val variant: String,
    val variantOption: String
)
