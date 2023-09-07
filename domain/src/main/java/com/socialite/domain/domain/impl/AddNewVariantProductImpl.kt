package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.domain.domain.AddNewVariantProduct
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.VariantProduct
import javax.inject.Inject

class AddNewVariantProductImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : AddNewVariantProduct {
    override suspend fun invoke(variantProduct: VariantProduct) {
        productVariantsRepository.insertVariantProduct(variantProduct.toData())
    }
}