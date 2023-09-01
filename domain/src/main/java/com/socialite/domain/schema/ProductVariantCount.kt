package com.socialite.domain.schema

import com.socialite.domain.schema.main.Product

data class ProductVariantCount(
    val product: Product,
    val variantCount: Int
)
