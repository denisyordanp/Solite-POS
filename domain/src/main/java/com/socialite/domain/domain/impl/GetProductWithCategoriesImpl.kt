package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetProductWithCategoriesImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetProductWithCategories {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke() = productsRepository.getActiveProductsWithCategory()
        .mapLatest {
            it.map { product ->
                val hasVariant = productVariantsRepository
                    .isProductHasVariants(product.product.id)
                product.copy(
                    hasVariant = hasVariant
                ).toDomain()
            }.groupBy { product ->
                product.category
            }.filterKeys { category ->
                category.isActive
            }
        }.flowOn(dispatcher)
}
