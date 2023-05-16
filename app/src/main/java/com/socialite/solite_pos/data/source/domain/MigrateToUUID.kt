package com.socialite.solite_pos.data.source.domain

interface MigrateToUUID {
    suspend operator fun invoke()
}