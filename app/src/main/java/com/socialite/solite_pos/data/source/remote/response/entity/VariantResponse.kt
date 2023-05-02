package com.socialite.solite_pos.data.source.remote.response.entity

data class VariantResponse(
    val id: String,
    val isMix: Boolean,
    val isMust: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val type: Int
)
