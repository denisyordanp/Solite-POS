package com.socialite.solite_pos.data.domain.impl

import com.socialite.solite_pos.data.domain.RegisterUser
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
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
