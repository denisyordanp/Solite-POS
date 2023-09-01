package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.domain.domain.RemoveVariantProduct
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.VariantProduct
import javax.inject.Inject

class RemoveVariantProductImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : RemoveVariantProduct {
    override suspend fun invoke(variantProduct: VariantProduct) {
        productVariantsRepository.removeVariantProduct(variantProduct.toData())
    }
}