package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductById
import javax.inject.Inject

class GetProductByIdImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : GetProductById {
    override fun invoke(productId: String) = productsRepository.getProductById(productId)
}