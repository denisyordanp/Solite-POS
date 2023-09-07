package com.socialite.domain.schema

import com.socialite.domain.schema.main.Category
import com.socialite.domain.schema.main.Product

data class ProductWithCategory(
    val product: Product,
    val category: Category,
    val hasVariant: Boolean
)
