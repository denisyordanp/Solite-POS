package com.socialite.domain.domain

import com.socialite.domain.schema.main.Store

fun interface AddNewStore {
    suspend operator fun invoke(store: Store)
}