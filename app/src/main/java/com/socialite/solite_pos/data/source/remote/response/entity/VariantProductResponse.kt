package com.socialite.solite_pos.data.source.remote.response.entity

data class VariantProductResponse(
    val id: String,
    val isUploaded: Boolean,
    val product: Int,
    val variant: Int,
    val variantOption: Int
)
