package com.socialite.domain.domain

import kotlinx.coroutines.flow.Flow

fun interface IsUserStaff {
    operator fun invoke(): Flow<Boolean>
}