package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.source.local.entity.helper.VariantWithOptions
import com.socialite.solite_pos.data.source.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.source.repository.VariantsRepository
import kotlinx.coroutines.flow.combine

class GetVariantsWithOptionsImpl(
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository
) : GetVariantsWithOptions {

    override fun invoke() = variantsRepository.getVariants()
        .combine(variantOptionsRepository.getVariantOptions()) { variants, options ->
            variants.map { variant ->
                val variantWithOptions = options.filter { it.variant == variant.id }
                VariantWithOptions(
                    variant = variant,
                    options = variantWithOptions
                )
            }
        }
}