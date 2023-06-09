package com.socialite.solite_pos.data.source.domain

fun interface Synchronize {
    suspend operator fun invoke(): Boolean
}
