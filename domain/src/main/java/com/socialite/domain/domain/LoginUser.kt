package com.socialite.domain.domain

fun interface LoginUser {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Boolean
}
