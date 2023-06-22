package com.socialite.solite_pos.data.source.domain

fun interface MigrateToUUID {
    suspend operator fun invoke()
}
