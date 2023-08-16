package com.socialite.solite_pos.data.domain

fun interface RegisterUser {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Boolean
}
