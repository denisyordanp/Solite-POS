package com.socialite.domain.domain.impl

import com.socialite.data.repository.VariantOptionsRepository
import com.socialite.data.schema.room.new_master.VariantOption
import com.socialite.domain.domain.AddNewVariantOption
import javax.inject.Inject

class AddNewVariantOptionImpl @Inject constructor(
    private val variantOptionsRepository: VariantOptionsRepository,
): AddNewVariantOption {
    override suspend fun invoke(option: VariantOption) {
        variantOptionsRepository.insertVariantOption(option)
    }
}