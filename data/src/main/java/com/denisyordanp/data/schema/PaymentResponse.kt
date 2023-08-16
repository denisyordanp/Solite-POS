package com.denisyordanp.data.schema

data class PaymentResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCash: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val tax: Float
)