package com.socialite.domain.domain

import com.socialite.data.schema.room.new_master.Product
import kotlinx.coroutines.flow.Flow

fun interface GetProductById {
    operator fun invoke(productId: String): Flow<Product>
}