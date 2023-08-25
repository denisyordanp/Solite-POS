package com.socialite.domain.domain

fun interface SetNewToken {
    operator fun invoke(token: String)
}