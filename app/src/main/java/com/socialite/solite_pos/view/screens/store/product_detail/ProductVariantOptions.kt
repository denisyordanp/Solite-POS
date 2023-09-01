package com.socialite.solite_pos.view.screens.store.product_detail

import com.socialite.domain.schema.ProductWithCategory
import com.socialite.domain.schema.VariantWithOptions

data class ProductVariantOptions(
    val product: ProductWithCategory,
    val variants: List<VariantWithOptions>?
)
