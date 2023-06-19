package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.helper.ProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetCategoryProductVariantCountImpl(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
) : GetCategoryProductVariantCount {
    override fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>> {
        return productsRepository.getAllProductsWithCategory()
            .map {
                it.groupBy { product ->
                    product.category
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
                                product = product.product,
                                variantCount = currentOptions
                            )
                        }
                    )
                }
            }
    }
}
