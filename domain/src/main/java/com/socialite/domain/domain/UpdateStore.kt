package com.socialite.domain.domain

import com.socialite.domain.schema.main.Store

fun interface UpdateStore {
    suspend operator fun invoke(store: Store)
}