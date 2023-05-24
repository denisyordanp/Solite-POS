package com.socialite.solite_pos.data.source.repository

fun interface Synchronize {
    suspend operator fun invoke(): Boolean
}
