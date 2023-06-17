package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductWithCategories
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.map

class GetProductWithCategoriesImpl(
    private val productsRepository: ProductsRepository
) : GetProductWithCategories {
    override fun invoke() = productsRepository.getAllProductWithCategories().map {
        it.groupBy { product -> product.category }
            .filterKeys { category -> category.isActive }
    }
}
