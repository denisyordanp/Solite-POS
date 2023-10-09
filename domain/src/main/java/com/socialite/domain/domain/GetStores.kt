package com.socialite.domain.domain

import com.socialite.schema.ui.main.Store
import kotlinx.coroutines.flow.Flow

fun interface GetStores {
    operator fun invoke(): Flow<List<Store>>
}