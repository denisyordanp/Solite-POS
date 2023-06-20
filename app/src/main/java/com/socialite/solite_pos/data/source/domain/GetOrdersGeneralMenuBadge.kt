package com.socialite.solite_pos.data.source.domain

import com.socialite.solite_pos.data.source.local.entity.helper.GeneralMenuBadge
import kotlinx.coroutines.flow.Flow

interface GetOrdersGeneralMenuBadge {
    operator fun invoke(date: String): Flow<List<GeneralMenuBadge>>
}
