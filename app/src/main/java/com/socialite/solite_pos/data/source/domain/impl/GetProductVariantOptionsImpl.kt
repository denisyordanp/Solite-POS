package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetProductVariantOptions
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.local.entity.room.helper.VariantProductWithOption
import com.socialite.solite_pos.data.source.local.room.ProductVariantsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductVariantOptionsImpl(
    private val dao: ProductVariantsDao
) : GetProductVariantOptions {
    override fun invoke(idProduct: Long): Flow<List<VariantWithOptions>?> {
        return dao.getVariantProducts(idProduct)
            .map {
                handleVariants(it)
            }
    }

    private fun handleVariants(variantProducts: List<VariantProductWithOption>?) =
        variantProducts?.groupBy { it.variant }
            ?.map {
                VariantWithOptions(
                    variant = it.key,
                    options = it.value.map { option ->
                        option.option
                    }
                )
            }
}
