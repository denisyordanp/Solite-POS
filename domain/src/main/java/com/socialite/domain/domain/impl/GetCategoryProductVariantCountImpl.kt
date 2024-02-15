package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.domain.GetCategoryProductVariantCount
import com.socialite.domain.helper.toDomain
import com.socialite.schema.ui.helper.ProductVariantCount
import com.socialite.schema.ui.main.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetCategoryProductVariantCountImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetCategoryProductVariantCount {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>> {
        return productsRepository.getAllProductsWithCategory()
            .mapLatest {
                it.groupBy { product ->
                    product.category.toDomain()
                }.filterKeys { category ->
                    category.isActive
                }.toList()
            }.combine(
                productVariantsRepository.getAllVariantOptions()
            ) { products, options ->
                products.map { maps ->
                    Pair(
                        first = maps.first,
                        second = maps.second.map { product ->
                            val currentOptions =
                                options.filter { it.variantProduct.product == product.product.id }.size
                            ProductVariantCount(
                                product = product.product.toDomain(),
                                variantCount = currentOptions
                            )
                        }
                    )
                }
            }.flowOn(dispatcher)
    }
}
