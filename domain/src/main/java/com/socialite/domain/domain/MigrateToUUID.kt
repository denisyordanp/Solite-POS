package com.socialite.domain.domain

fun interface MigrateToUUID {
    suspend operator fun invoke()
}
