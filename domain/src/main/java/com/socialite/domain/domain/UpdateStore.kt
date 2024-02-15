package com.socialite.domain.domain

import com.socialite.schema.ui.main.Store

fun interface UpdateStore {
    suspend operator fun invoke(store: Store)
}