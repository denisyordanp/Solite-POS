package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.Logout
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LogoutImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : Logout {
    override fun invoke() = dataStateFlow(dispatcher) {
        settingRepository.insertToken("")
        userRepository.saveLoggedInUser(null)
    }
}