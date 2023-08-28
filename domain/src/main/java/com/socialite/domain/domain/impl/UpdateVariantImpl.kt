package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantsRepository
import com.socialite.data.schema.room.new_master.Variant
import com.socialite.domain.domain.UpdateVariant
import javax.inject.Inject

class UpdateVariantImpl @Inject constructor(
    private val variantsRepository: VariantsRepository,
): UpdateVariant {
    override suspend fun invoke(variant: Variant) {
        variantsRepository.updateVariant(variant)
    }
}