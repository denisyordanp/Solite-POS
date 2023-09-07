package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.UpdateProduct
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Product
import javax.inject.Inject

class UpdateProductImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : UpdateProduct {
    override suspend fun invoke(product: Product) {
        productsRepository.updateProduct(product.toData())
    }
}