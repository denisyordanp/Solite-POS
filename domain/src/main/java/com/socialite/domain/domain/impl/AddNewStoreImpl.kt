package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.AddNewStore
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddNewStoreImpl @Inject constructor(
    private val storeRepository: StoreRepository,
    private val settingRepository: SettingRepository,
) : AddNewStore {
    override suspend fun invoke(store: Store) {
        storeRepository.insertStore(store.toData())
        if (settingRepository.getNewSelectedStore().first().isEmpty()) {
            settingRepository.selectNewStore(store.id).collect()
        }
    }
}