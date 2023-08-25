package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.SetDarkMode
import javax.inject.Inject

class SetDarkModeImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : SetDarkMode {
    override suspend fun invoke(isActive: Boolean) {
        settingRepository.setDarkMode(isActive)
    }
}