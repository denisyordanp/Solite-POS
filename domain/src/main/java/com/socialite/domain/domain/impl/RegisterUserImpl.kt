package com.socialite.domain.domain.impl

import com.socialite.domain.domain.RegisterUser
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import javax.inject.Inject

class RegisterUserImpl @Inject constructor(
    private val repository: AccountRepository,
    private val settingRepository: SettingRepository
) : RegisterUser {
    override suspend fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Boolean {
        val userToken = repository.register(name, email, password, storeName)
        settingRepository.insertToken(userToken)
        return true
    }
}