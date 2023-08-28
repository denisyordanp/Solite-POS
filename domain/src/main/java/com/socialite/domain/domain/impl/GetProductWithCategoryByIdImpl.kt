package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductWithCategoryById
import javax.inject.Inject

class GetProductWithCategoryByIdImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : GetProductWithCategoryById {
    override fun invoke(productId: String) = productsRepository.getProductWithCategory(productId)
}