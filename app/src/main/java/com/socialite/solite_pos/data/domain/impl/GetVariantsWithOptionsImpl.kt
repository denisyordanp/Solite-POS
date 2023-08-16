package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.GetVariantsWithOptions
import com.socialite.solite_pos.data.schema.helper.VariantWithOptions
import com.socialite.solite_pos.data.repository.VariantOptionsRepository
import com.socialite.solite_pos.data.repository.VariantsRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetVariantsWithOptionsImpl @Inject constructor(
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
