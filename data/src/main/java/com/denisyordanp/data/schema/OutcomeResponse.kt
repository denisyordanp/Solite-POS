package com.denisyordanp.data.schema

data class OutcomeResponse(
    val amount: Int,
    val date: String,
    val desc: String,
    val id: String,
    val isUploaded: Boolean,
    val name: String,
    val price: Int,
    val store: String
)
