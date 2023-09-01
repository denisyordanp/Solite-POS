package com.socialite.domain.domain

import com.socialite.domain.schema.main.Variant

fun interface UpdateVariant {
    suspend operator fun invoke(variant: Variant)
}