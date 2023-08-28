package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.data.schema.room.new_master.Product
import com.socialite.domain.domain.UpdateProduct
import javax.inject.Inject

class UpdateProductImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : UpdateProduct {
    override suspend fun invoke(product: Product) {
        productsRepository.updateProduct(product)
    }
}