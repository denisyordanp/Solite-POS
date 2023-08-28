package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.GetSelectedStore
import javax.inject.Inject

class GetSelectedStoreImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : GetSelectedStore {
    override fun invoke() = settingRepository.getNewSelectedStore()
}