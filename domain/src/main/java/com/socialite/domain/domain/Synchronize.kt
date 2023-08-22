package com.socialite.domain.domain

fun interface Synchronize {
    suspend operator fun invoke(): Boolean
}
