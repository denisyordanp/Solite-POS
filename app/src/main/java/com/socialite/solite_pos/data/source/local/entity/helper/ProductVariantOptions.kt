package com.socialite.solite_pos.data.source.local.entity.helper

import com.socialite.solite_pos.data.source.local.entity.room.helper.ProductWithCategory

data class ProductVariantOptions(
    val product: ProductWithCategory,
    val variants: List<VariantWithOptions>?
)
