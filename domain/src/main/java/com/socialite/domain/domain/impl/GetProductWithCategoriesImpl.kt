package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetProductWithCategories
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductWithCategoriesImpl @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
) : GetProductWithCategories {
    override fun invoke() = productsRepository.getActiveProductsWithCategory()
        .map {
            it.map { product ->
                val hasVariant = productVariantsRepository
                    .isProductHasVariants(product.product.id)
                product.copy(
                    hasVariant = hasVariant
                )
            }.groupBy { product ->
                product.category
            }.filterKeys { category ->
                category.isActive
            }
        }
}
