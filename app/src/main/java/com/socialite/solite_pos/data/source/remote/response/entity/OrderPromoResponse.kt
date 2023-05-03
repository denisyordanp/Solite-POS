package com.socialite.solite_pos.data.source.remote.response.entity

data class OrderPromoResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val promo: String,
    val totalPromo: Int
)
