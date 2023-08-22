package com.socialite.domain.domain

import kotlinx.coroutines.flow.Flow

fun interface IsShouldSelectStore {
    operator fun invoke(): Flow<Boolean>
}