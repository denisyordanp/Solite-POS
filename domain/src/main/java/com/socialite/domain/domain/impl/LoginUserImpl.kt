package com.socialite.domain.domain.impl

import com.socialite.domain.domain.LoginUser
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import javax.inject.Inject

class LoginUserImpl @Inject constructor(
    private val repository: AccountRepository,
    private val settingRepository: SettingRepository
) : LoginUser {
    override suspend fun invoke(email: String, password: String): Boolean {
        val userToken = repository.login(email, password)
        settingRepository.insertToken(userToken)
        return true
    }

}