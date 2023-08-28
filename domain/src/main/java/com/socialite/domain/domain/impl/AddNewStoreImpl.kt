package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.data.schema.room.new_master.Store
import com.socialite.domain.domain.AddNewStore
import javax.inject.Inject

class AddNewStoreImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : AddNewStore {
    override suspend fun invoke(store: Store) {
        storeRepository.insertStore(store)
    }
}