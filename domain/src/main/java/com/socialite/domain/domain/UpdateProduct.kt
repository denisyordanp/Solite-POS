package com.socialite.domain.domain

import com.socialite.domain.schema.main.Product

fun interface UpdateProduct {
    suspend operator fun invoke(product: Product)
}