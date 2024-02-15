package com.socialite.domain.domain

import com.socialite.schema.ui.main.Variant

fun interface UpdateVariant {
    suspend operator fun invoke(variant: Variant)
}