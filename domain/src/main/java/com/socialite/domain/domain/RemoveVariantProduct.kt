package com.socialite.domain.domain

import com.socialite.domain.schema.main.VariantProduct

fun interface RemoveVariantProduct {
    suspend operator fun invoke(variantProduct: VariantProduct)
}