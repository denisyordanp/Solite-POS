package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.domain.domain.GetVariantsProductById
import javax.inject.Inject

class GetVariantsProductByIdImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : GetVariantsProductById {
    override fun invoke(productId: String) =
        productVariantsRepository.getVariantsProductById(productId)
}