package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetVariantOptions
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.repository.ProductVariantsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetVariantOptionsImpl(
    private val productVariantsRepository: ProductVariantsRepository
) : GetVariantOptions {
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
