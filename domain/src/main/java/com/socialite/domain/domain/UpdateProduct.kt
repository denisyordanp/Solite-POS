package com.socialite.domain.domain

import com.socialite.schema.ui.main.Product

fun interface UpdateProduct {
    suspend operator fun invoke(product: Product)
}