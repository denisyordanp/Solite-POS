package com.socialite.data.schema.response

data class ProductResponse(
    val category: String,
    val desc: String,
    val id: String,
    val image: String,
    val isActive: Boolean,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
)
