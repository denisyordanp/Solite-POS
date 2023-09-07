package com.socialite.domain.domain.impl

import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.GetToken
import javax.inject.Inject

class GetTokenImpl @Inject constructor(
    private val settingRepository: SettingRepository,
) : GetToken {
    override fun invoke() = settingRepository.getToken()
}