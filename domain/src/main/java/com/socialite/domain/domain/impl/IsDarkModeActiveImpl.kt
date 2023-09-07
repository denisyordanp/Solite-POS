package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.IsDarkModeActive
import javax.inject.Inject

class IsDarkModeActiveImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : IsDarkModeActive {
    override suspend fun invoke() = settingRepository.getIsDarkModeActive()
}