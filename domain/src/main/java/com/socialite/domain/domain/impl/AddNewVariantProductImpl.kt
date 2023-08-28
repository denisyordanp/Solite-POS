package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.domain.domain.AddNewVariantProduct
import javax.inject.Inject

class AddNewVariantProductImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : AddNewVariantProduct {
    override suspend fun invoke(variantProduct: VariantProduct) {
        productVariantsRepository.insertVariantProduct(variantProduct)
    }
}