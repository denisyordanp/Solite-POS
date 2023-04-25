package com.socialite.solite_pos.data.source.domain

interface RegisterUser {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): String
}