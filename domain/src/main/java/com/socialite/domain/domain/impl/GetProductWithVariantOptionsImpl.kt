package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetProductWithVariantOptions
import com.socialite.data.schema.room.helper.VariantProductWithOption
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.VariantWithOptions
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProductWithVariantOptionsImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
    private val productsRepository: ProductsRepository
) : GetProductWithVariantOptions {
    override fun invoke(productId: String) = productVariantsRepository.getVariantOptions(productId)
        .combine(productsRepository.getProductById(productId)) { variants, product ->
            Pair(
                first = product.toDomain(),
                second = variants?.handleVariants()
            )
        }

    private fun List<VariantProductWithOption>.handleVariants() =
        this.groupBy { it.variant }
            .map {
                VariantWithOptions(
                    variant = it.key.toDomain(),
                    options = it.value.map { option ->
                        option.option.toDomain()
                    }
                )
            }
}
