package com.socialite.data.schema.response

data class VariantOptionResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val variant: String
)