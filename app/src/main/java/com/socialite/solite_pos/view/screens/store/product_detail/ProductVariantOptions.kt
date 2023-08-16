package com.socialite.solite_pos.view.screens.store.product_detail

import com.socialite.solite_pos.data.schema.helper.VariantWithOptions
import com.socialite.data.schema.room.helper.ProductWithCategory

data class ProductVariantOptions(
    val product: ProductWithCategory,
    val variants: List<VariantWithOptions>?
)
