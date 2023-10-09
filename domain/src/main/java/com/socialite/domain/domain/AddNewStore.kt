package com.socialite.domain.domain

import com.socialite.schema.ui.main.Store

fun interface AddNewStore {
    suspend operator fun invoke(store: Store)
}