package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Product

fun interface UpdateProduct {
    suspend operator fun invoke(product: Product)
}