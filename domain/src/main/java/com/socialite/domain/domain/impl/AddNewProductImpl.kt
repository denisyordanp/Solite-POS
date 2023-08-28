package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.data.schema.room.new_master.Product
import com.socialite.domain.domain.AddNewProduct
import javax.inject.Inject

class AddNewProductImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : AddNewProduct {
    override suspend fun invoke(product: Product) {
        productsRepository.insertProduct(product)
    }
}