package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.map

class GetProductWithCategoriesImpl(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
) : GetProductWithCategories {
    override fun invoke() = productsRepository.getAllProductWithCategories()
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
