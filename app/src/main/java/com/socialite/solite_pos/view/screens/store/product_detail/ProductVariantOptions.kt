package com.socialite.solite_pos.view.screens.store.product_detail

import com.socialite.schema.ui.helper.ProductWithCategory
import com.socialite.schema.ui.helper.VariantWithOptions

data class ProductVariantOptions(
    val product: ProductWithCategory,
    val variants: List<VariantWithOptions>?
)
