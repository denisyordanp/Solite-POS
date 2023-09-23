package com.socialite.domain.domain

import com.socialite.common.menus.StoreMenus
import kotlinx.coroutines.flow.Flow

fun interface GetStoreMenus {
    operator fun invoke(): Flow<List<StoreMenus>>
}