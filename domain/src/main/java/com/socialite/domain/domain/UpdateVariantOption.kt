package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.VariantOption

fun interface UpdateVariantOption {
    suspend operator fun invoke(option: VariantOption)
}