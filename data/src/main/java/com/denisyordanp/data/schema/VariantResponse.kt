package com.denisyordanp.data.schema

data class VariantResponse(
    val id: String,
    val isMust: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val type: Int
)
