package com.socialite.solite_pos.data.schema.helper

import com.socialite.data.schema.room.new_master.Product

data class ProductVariantCount(
    val product: Product,
    val variantCount: Int
)
