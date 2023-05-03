package com.socialite.solite_pos.data.source.remote.response.entity

data class VariantProductResponse(
    val id: String,
    val isUploaded: Boolean,
    val product: String,
    val variant: String,
    val variantOption: String
)
