package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.SetNewToken
import javax.inject.Inject

class SetNewTokenImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : SetNewToken {
    override fun invoke(token: String) {
        settingRepository.insertToken(token)
    }
}