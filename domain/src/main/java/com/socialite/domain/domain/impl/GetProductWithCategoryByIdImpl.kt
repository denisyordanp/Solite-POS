package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductWithCategoryById
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductWithCategoryByIdImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetProductWithCategoryById {
    override fun invoke(productId: String) =
        productsRepository.getProductWithCategory(productId).map { it?.toDomain() }.flowOn(dispatcher)
}