package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Promo

fun interface AddNewPromo {
    suspend operator fun invoke(promo: Promo)
}