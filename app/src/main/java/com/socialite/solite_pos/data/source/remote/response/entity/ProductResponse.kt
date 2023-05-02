package com.socialite.solite_pos.data.source.remote.response.entity

data class ProductResponse(
    val buyPrice: Int,
    val category: Int,
    val desc: String,
    val id: String,
    val image: String,
    val isActive: Boolean,
    val isMix: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val portion: Int,
    val sellPrice: Int,
    val stock: Int
)
