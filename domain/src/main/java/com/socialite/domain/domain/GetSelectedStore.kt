package com.socialite.domain.domain

import kotlinx.coroutines.flow.Flow

fun interface GetSelectedStore {
    operator fun invoke(): Flow<String>
}