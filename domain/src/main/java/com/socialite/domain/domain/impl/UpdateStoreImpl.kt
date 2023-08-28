package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.data.schema.room.new_master.Store
import com.socialite.domain.domain.UpdateStore
import javax.inject.Inject

class UpdateStoreImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : UpdateStore {
    override suspend fun invoke(store: Store) {
        storeRepository.updateStore(store)
    }
}