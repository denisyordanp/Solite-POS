package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.AddNewStore
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Store
import javax.inject.Inject

class AddNewStoreImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : AddNewStore {
    override suspend fun invoke(store: Store) {
        storeRepository.insertStore(store.toData())
    }
}