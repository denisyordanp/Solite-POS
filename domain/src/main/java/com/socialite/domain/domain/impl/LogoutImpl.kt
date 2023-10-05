package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.DefaultDispatcher
import com.socialite.common.utility.extension.dataStateFlow
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.Logout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class LogoutImpl @Inject constructor(
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : Logout {
    override fun invoke() = dataStateFlow(dispatcher) {
        settingRepository.insertToken("")
        userRepository.saveLoggedInUser(null).combine(
            settingRepository.selectNewStore("")
        ) { _, _ -> true }
    }
}