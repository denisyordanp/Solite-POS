package com.socialite.domain.domain

import com.socialite.schema.ui.main.VariantProduct

fun interface AddNewVariantProduct {
    suspend operator fun invoke(variantProduct: VariantProduct)
}