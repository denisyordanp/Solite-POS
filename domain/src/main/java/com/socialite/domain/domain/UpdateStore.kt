package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Store

fun interface UpdateStore {
    suspend operator fun invoke(store: Store)
}