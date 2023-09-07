package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductById
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductByIdImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetProductById {
    override fun invoke(productId: String) =
        productsRepository.getProductById(productId).map { it.toDomain() }.flowOn(dispatcher)
}