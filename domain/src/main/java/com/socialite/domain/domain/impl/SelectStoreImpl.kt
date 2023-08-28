package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.SelectStore
import javax.inject.Inject

class SelectStoreImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : SelectStore {
    override suspend fun invoke(storeId: String) {
        settingRepository.selectNewStore(storeId)
    }
}