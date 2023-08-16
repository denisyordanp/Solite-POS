package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.schema.helper.VariantWithOptions
import com.socialite.data.schema.room.helper.VariantProductWithOption
import com.socialite.data.repository.ProductVariantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductVariantOptionsImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository
) : GetProductVariantOptions {
    override fun invoke(idProduct: String): Flow<List<VariantWithOptions>?> {
        return productVariantsRepository.getVariantOptions(idProduct)
            .map { it?.handleVariants() }
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
