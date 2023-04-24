package com.socialite.solite_pos.data.source.domain

interface LoginUser {
    suspend operator fun invoke(
        email: String,
        password: String
    ): String
}