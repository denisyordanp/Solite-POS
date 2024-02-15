package com.socialite.schema.ui.helper

import com.socialite.schema.ui.main.Category
import com.socialite.schema.ui.main.Product

data class ProductWithCategory(
    val product: Product,
    val category: Category,
    val hasVariant: Boolean
)
