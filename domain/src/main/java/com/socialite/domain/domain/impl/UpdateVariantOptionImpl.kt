package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.schema.room.new_master.VariantOption
import com.socialite.domain.domain.UpdateVariantOption
import javax.inject.Inject

class UpdateVariantOptionImpl @Inject constructor(
    private val variantOptionsRepository: VariantOptionsRepository,
) : UpdateVariantOption {
    override suspend fun invoke(option: VariantOption) {
        variantOptionsRepository.updateVariantOption(option)
    }
}