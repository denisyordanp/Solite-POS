package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantsRepository
import com.socialite.data.schema.room.new_master.Variant
import com.socialite.domain.domain.AddNewVariant
import javax.inject.Inject

class AddNewVariantImpl @Inject constructor(
    private val variantsRepository: VariantsRepository,
): AddNewVariant {
    override suspend fun invoke(variant: Variant) {
        variantsRepository.insertVariant(variant)
    }
}