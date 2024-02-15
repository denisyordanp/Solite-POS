package com.socialite.domain.domain.impl

import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.UpdateStore
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Store
import javax.inject.Inject

class UpdateStoreImpl @Inject constructor(
    private val storeRepository: StoreRepository,
) : UpdateStore {
    override suspend fun invoke(store: Store) {
        storeRepository.updateStore(store.toData())
    }
}