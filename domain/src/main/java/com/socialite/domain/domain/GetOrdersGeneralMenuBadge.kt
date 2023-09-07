package com.socialite.domain.domain

import com.socialite.domain.schema.GeneralMenuBadge
import kotlinx.coroutines.flow.Flow

fun interface GetOrdersGeneralMenuBadge {
    operator fun invoke(date: String): Flow<List<GeneralMenuBadge>>
}
