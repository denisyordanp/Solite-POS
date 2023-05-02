package com.socialite.solite_pos.data.source.remote.response.entity

data class CategoryResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isStock: Boolean,
    val isUploaded: Boolean,
    val name: String
)
