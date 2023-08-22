package com.socialite.domain.domain

fun interface RegisterUser {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Boolean
}
