package com.socialite.domain.domain

import com.socialite.schema.ui.main.Store
import kotlinx.coroutines.flow.Flow

fun interface GetSelectedStore {
    operator fun invoke(): Flow<Store?>
}