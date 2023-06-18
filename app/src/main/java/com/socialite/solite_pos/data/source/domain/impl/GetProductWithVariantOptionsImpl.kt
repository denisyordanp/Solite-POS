package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductWithVariantOptions
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import com.socialite.solite_pos.data.source.repository.ProductsRepository
import kotlinx.coroutines.flow.combine

class GetProductWithVariantOptionsImpl(
    private val productVariantsRepository: ProductVariantsRepository,
    private val productsRepository: ProductsRepository
) : GetProductWithVariantOptions {
    override fun invoke(productId: String) = productVariantsRepository.getVariantOptions(productId)
        .combine(productsRepository.getProductById(productId)) { variants, product ->
            Pair(
                first = product,
                second = variants?.handleVariants()
            )
        }

    private fun List<VariantProductWithOption>.handleVariants() =
        this.groupBy { it.variant }
            .map {
                VariantWithOptions(
                    variant = it.key,
                    options = it.value.map { option ->
                        option.option
                    }
                )
            }
}