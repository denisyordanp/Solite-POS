package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Variant

fun interface UpdateVariant {
    suspend operator fun invoke(variant: Variant)
}