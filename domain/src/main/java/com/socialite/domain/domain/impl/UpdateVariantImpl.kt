package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantsRepository
import com.socialite.domain.domain.UpdateVariant
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Variant
import javax.inject.Inject

class UpdateVariantImpl @Inject constructor(
    private val variantsRepository: VariantsRepository,
): UpdateVariant {
    override suspend fun invoke(variant: Variant) {
        variantsRepository.updateVariant(variant.toData())
    }
}