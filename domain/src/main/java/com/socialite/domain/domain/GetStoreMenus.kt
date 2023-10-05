package com.socialite.domain.domain

import com.socialite.schema.menu.StoreMenus
import kotlinx.coroutines.flow.Flow

fun interface GetStoreMenus {
    operator fun invoke(): Flow<List<StoreMenus>>
}