package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository

class RegisterUserImpl(
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
