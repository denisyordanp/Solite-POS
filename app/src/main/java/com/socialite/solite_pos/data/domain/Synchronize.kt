package com.socialite.solite_pos.data.domain

fun interface Synchronize {
    suspend operator fun invoke(): Boolean
}
