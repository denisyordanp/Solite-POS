package com.socialite.domain.domain
import com.socialite.schema.ui.main.Product
import kotlinx.coroutines.flow.Flow

fun interface GetProductById {
    operator fun invoke(productId: String): Flow<Product>
}