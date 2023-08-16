package com.socialite.solite_pos.data.schema.helper

import com.socialite.solite_pos.data.schema.room.new_master.Product

data class ProductVariantCount(
    val product: Product,
    val variantCount: Int
)
