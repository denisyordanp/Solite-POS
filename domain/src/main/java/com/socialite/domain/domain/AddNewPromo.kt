package com.socialite.domain.domain

import com.socialite.domain.schema.main.Promo

fun interface AddNewPromo {
    suspend operator fun invoke(promo: Promo)
}