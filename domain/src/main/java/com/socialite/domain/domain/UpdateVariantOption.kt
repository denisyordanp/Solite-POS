package com.socialite.domain.domain

import com.socialite.schema.ui.main.VariantOption

fun interface UpdateVariantOption {
    suspend operator fun invoke(option: VariantOption)
}