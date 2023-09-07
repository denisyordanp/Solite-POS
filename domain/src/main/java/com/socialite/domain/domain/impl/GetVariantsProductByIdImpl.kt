package com.socialite.domain.domain.impl

import com.socialite.data.repository.ProductVariantsRepository
import com.socialite.domain.domain.GetVariantsProductById
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetVariantsProductByIdImpl @Inject constructor(
    private val productVariantsRepository: ProductVariantsRepository,
) : GetVariantsProductById {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(productId: String) =
        productVariantsRepository.getVariantsProductById(productId)
            .mapLatest { variants -> variants.map { it.toDomain() } }
}