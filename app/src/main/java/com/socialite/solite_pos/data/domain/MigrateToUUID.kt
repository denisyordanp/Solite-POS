package com.socialite.solite_pos.data.domain

fun interface MigrateToUUID {
    suspend operator fun invoke()
}
