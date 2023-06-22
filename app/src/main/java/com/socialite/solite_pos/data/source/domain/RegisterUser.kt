package com.socialite.solite_pos.data.source.domain

fun interface RegisterUser {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Boolean
}
