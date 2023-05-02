package com.socialite.solite_pos.data.source.remote.response.entity

data class PromoResponse(
    val desc: String,
    val id: String,
    val isActive: Boolean,
    val isCash: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val value: Any
)
