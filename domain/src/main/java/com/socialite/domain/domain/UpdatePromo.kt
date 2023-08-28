package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Promo

fun interface UpdatePromo {
    suspend operator fun invoke(promo: Promo)
}