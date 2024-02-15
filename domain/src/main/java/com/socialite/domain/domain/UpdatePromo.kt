package com.socialite.domain.domain

import com.socialite.schema.ui.main.Promo

fun interface UpdatePromo {
    suspend operator fun invoke(promo: Promo)
}