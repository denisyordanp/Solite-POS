package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.domain.domain.UpdateVariantOption
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.VariantOption
import javax.inject.Inject

class UpdateVariantOptionImpl @Inject constructor(
    private val variantOptionsRepository: VariantOptionsRepository,
) : UpdateVariantOption {
    override suspend fun invoke(option: VariantOption) {
        variantOptionsRepository.updateVariantOption(option.toData())
    }
}