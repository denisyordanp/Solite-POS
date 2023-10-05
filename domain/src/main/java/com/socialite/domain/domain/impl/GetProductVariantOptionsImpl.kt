package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.schema.room.helper.VariantProductWithOption
import com.socialite.domain.domain.GetProductVariantOptions
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.VariantWithOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductVariantOptionsImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetProductVariantOptions {
    override fun invoke(idProduct: String): Flow<List<VariantWithOptions>?> {
        return productVariantsRepository.getVariantOptions(idProduct)
            .map { it?.handleVariants() }.flowOn(dispatcher)
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
