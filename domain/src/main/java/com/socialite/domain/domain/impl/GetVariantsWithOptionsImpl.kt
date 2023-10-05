package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.repository.VariantsRepository
import com.socialite.domain.domain.GetVariantsWithOptions
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.VariantWithOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetVariantsWithOptionsImpl @Inject constructor(
    private val variantsRepository: VariantsRepository,
    private val variantOptionsRepository: VariantOptionsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetVariantsWithOptions {

    override fun invoke() = variantsRepository.getVariants()
        .combine(variantOptionsRepository.getVariantOptions()) { variants, options ->
            variants.map { variant ->
                val variantWithOptions = options.filter { it.variant == variant.id }
                VariantWithOptions(
                    variant = variant.toDomain(),
                    options = variantWithOptions.map { it.toDomain() }
                )
            }
        }.flowOn(dispatcher)
}
