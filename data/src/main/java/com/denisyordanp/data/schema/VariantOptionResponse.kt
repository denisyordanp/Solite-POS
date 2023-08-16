package com.denisyordanp.data.schema

data class VariantOptionResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val variant: String
)