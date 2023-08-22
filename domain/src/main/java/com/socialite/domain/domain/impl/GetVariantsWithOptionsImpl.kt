package com.socialite.domain.domain.impl

import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.domain.schema.helper.VariantWithOptions
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
