package com.socialite.solite_pos.data.source.remote.response.entity

data class VariantOptionResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCount: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val variant: Int
)
