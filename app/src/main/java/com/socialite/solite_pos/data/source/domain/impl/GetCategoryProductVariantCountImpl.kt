package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetCategoryProductVariantCount
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Category
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import com.socialite.solite_pos.data.source.local.entity.helper.ProductVariantCount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class GetCategoryProductVariantCountImpl(
    private val productsRepository: ProductsRepository,
    private val productVariantsRepository: ProductVariantsRepository,
) : GetCategoryProductVariantCount {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<List<Pair<Category, List<ProductVariantCount>>>> {
        return productsRepository.getAllProductsWithCategory()
            .flatMapLatest {
                flow {
                    val newItem = it.groupBy { product ->
                        product.category
                    }.filterKeys { category ->
                        category.isActive
                    }.toList()
                        .map { maps ->
                            Pair(
                                first = maps.first,
                                second = maps.second.map { product ->
                                    val variantCounts =
                                        productVariantsRepository.getVariantOptions(product.product.id)
                                            .first()?.count() ?: 0
                                    ProductVariantCount(
                                        product = product.product,
                                        variantCount = variantCounts
                                    )
                                }
                            )
                        }
                    emit(newItem)
                }
            }
    }
}
