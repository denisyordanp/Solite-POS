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

    private fun handleVariants(variantProducts: List<VariantProductWithOption>?): List<VariantWithOptions> {
        val variants: ArrayList<VariantWithOptions> = ArrayList()
        if (!variantProducts.isNullOrEmpty()) {
            var data = VariantWithOptions()
            for ((i, v) in variantProducts.withIndex()) {

                when {
                    variantProducts.size - 1 == i -> {
                        if (data.variant != null) {
                            if (v.variant == data.variant!!) {
                                data.options.add(v.option)
                                variants.add(data)
                            } else {
                                variants.add(data)
                                data = VariantWithOptions()
                                data.variant = v.variant
                                data.options.add(v.option)
                                variants.add(data)
                            }
                        } else {
                            data.variant = v.variant
                            data.options.add(v.option)
                            variants.add(data)
                        }
                    }

                    i == 0 -> {
                        data.variant = v.variant
                        data.options.add(v.option)
                    }

                    data.variant != null -> {
                        if (v.variant == data.variant!!) {
                            data.options.add(v.option)
                        } else {
                            variants.add(data)
                            data = VariantWithOptions()
                            data.variant = v.variant
                            data.options.add(v.option)
                        }
                    }
                }
            }
        }
        return variants
    }
}
