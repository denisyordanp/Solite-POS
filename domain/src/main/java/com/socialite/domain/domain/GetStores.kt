package com.socialite.domain.domain

import com.socialite.domain.schema.main.Store
import kotlinx.coroutines.flow.Flow

fun interface GetStores {
    operator fun invoke(): Flow<List<Store>>
}