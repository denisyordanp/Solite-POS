package com.socialite.domain.domain

import com.socialite.domain.schema.main.VariantOption

fun interface AddNewVariantOption {
    suspend operator fun invoke(option: VariantOption)
}