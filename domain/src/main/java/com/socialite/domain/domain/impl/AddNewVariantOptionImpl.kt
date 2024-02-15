package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.domain.domain.AddNewVariantOption
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.VariantOption
import javax.inject.Inject

class AddNewVariantOptionImpl @Inject constructor(
    private val variantOptionsRepository: VariantOptionsRepository,
): AddNewVariantOption {
    override suspend fun invoke(option: VariantOption) {
        variantOptionsRepository.insertVariantOption(option.toData())
    }
}