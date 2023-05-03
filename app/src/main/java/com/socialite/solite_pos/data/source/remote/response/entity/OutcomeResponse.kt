package com.socialite.solite_pos.data.source.remote.response.entity

data class OutcomeResponse(
    val amount: Int,
    val date: String,
    val desc: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
    val store: Int
)
