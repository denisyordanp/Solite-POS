package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.GetStores
import javax.inject.Inject

class GetStoresImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : GetStores {
    override fun invoke() = storeRepository.getStores()
}