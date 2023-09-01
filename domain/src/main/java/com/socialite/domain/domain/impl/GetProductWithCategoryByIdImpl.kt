package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductWithCategoryById
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductWithCategoryByIdImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
) : GetProductWithCategoryById {
    override fun invoke(productId: String) =
        productsRepository.getProductWithCategory(productId).map { it?.toDomain() }
}