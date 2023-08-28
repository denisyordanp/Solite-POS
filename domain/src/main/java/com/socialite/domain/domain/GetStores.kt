package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Store
import kotlinx.coroutines.flow.Flow

fun interface GetStores {
    operator fun invoke(): Flow<List<Store>>
}