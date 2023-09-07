package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.domain.domain.GetProductWithVariantOptions
import com.socialite.data.schema.room.helper.VariantProductWithOption
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.repository.ProductsRepository
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.VariantWithOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetProductWithVariantOptionsImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
    private val productsRepository: ProductsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetProductWithVariantOptions {
    override fun invoke(productId: String) = productVariantsRepository.getVariantOptions(productId)
        .combine(productsRepository.getProductById(productId)) { variants, product ->
            Pair(
                first = product.toDomain(),
                second = variants?.handleVariants()
            )
        }.flowOn(dispatcher)

    private fun List<VariantProductWithOption>.handleVariants() =
        this.groupBy { it.variant }
            .map {
                val activeOptions = it.value
                    .filter { option ->
                        option.option.isActive
                    }
                    .map { option ->
                        option.option.toDomain()
                    }

                VariantWithOptions(
                    variant = it.key.toDomain(),
                    options = activeOptions
                )
            }.filter {
                it.options.isNotEmpty()
            }
}
