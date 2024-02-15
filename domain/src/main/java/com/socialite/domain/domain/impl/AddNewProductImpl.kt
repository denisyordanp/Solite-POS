package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.AddNewProduct
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Product
import javax.inject.Inject

class AddNewProductImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : AddNewProduct {
    override suspend fun invoke(product: Product) {
        productsRepository.insertProduct(product.toData())
    }
}