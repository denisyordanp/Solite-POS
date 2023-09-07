package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.GetStores
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetStoresImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : GetStores {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke() =
        storeRepository.getStores().mapLatest { it.map { stores -> stores.toDomain() } }
}