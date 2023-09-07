package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantsRepository
import com.socialite.domain.domain.AddNewVariant
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Variant
import javax.inject.Inject

class AddNewVariantImpl @Inject constructor(
    private val variantsRepository: VariantsRepository,
): AddNewVariant {
    override suspend fun invoke(variant: Variant) {
        variantsRepository.insertVariant(variant.toData())
    }
}