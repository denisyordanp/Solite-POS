package com.socialite.solite_pos.data.domain

fun interface LoginUser {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Boolean
}
