package com.socialite.domain.domain

import com.socialite.domain.schema.main.VariantOption

fun interface UpdateVariantOption {
    suspend operator fun invoke(option: VariantOption)
}