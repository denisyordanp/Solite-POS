package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.data.schema.room.new_bridge.VariantProduct
import com.socialite.domain.domain.RemoveVariantProduct
import javax.inject.Inject

class RemoveVariantProductImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : RemoveVariantProduct {
    override suspend fun invoke(variantProduct: VariantProduct) {
        productVariantsRepository.removeVariantProduct(variantProduct)
    }
}