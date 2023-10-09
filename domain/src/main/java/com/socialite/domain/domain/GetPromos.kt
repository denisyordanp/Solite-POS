package com.socialite.domain.domain

import com.socialite.schema.ui.main.Promo
import kotlinx.coroutines.flow.Flow

fun interface GetPromos {
    operator fun invoke(status: Promo.Status): Flow<List<Promo>>
}