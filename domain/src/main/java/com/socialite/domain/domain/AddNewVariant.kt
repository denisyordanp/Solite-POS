package com.socialite.domain.domain

import com.socialite.schema.ui.main.Variant

fun interface AddNewVariant {
    suspend operator fun invoke(variant: Variant)
}