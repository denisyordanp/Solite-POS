package com.denisyordanp.data.schema

data class OrderPromoResponse(
    val id: String,
    val isUploaded: Boolean,
    val order: String,
    val promo: String,
    val totalPromo: Int
)
