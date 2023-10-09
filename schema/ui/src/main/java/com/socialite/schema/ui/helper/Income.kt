package com.socialite.schema.ui.helper

data class Income(
    val date: String,
    val payment: String,
    val total: Long,
    val isCash: Boolean
)
