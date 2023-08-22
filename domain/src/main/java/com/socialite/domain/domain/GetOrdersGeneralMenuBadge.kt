package com.socialite.domain.domain

import com.socialite.domain.schema.helper.GeneralMenuBadge
import kotlinx.coroutines.flow.Flow

fun interface GetOrdersGeneralMenuBadge {
    operator fun invoke(date: String): Flow<List<GeneralMenuBadge>>
}
