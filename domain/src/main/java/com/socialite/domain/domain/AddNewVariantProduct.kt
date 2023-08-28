package com.socialite.domain.domain

import com.socialite.data.schema.room.new_bridge.VariantProduct

fun interface AddNewVariantProduct {
    suspend operator fun invoke(variantProduct: VariantProduct)
}