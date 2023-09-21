package com.socialite.domain.domain

import kotlinx.coroutines.flow.Flow

fun interface GetSelectedStoreId {
    operator fun invoke(): Flow<String>
}