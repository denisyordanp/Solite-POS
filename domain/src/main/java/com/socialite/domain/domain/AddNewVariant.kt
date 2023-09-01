package com.socialite.domain.domain

import com.socialite.domain.schema.main.Variant

fun interface AddNewVariant {
    suspend operator fun invoke(variant: Variant)
}