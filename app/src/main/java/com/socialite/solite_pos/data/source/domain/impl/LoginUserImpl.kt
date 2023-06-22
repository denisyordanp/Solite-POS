package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.repository.AccountRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
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
