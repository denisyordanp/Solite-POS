package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetCategoryProductVariantCount
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.ProductVariantCount
import com.socialite.domain.schema.main.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoryProductVariantCountImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
) : GetCategoryProductVariantCount {
    override fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>> {
        return productsRepository.getAllProductsWithCategory()
            .map {
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
            }
    }
}
