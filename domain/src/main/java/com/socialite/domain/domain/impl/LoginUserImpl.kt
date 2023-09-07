package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.state.DataState
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.domain.domain.LoginUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoginUserImpl @Inject constructor(
    private val repository: AccountRepository,
    private val settingRepository: SettingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : LoginUser {
    override suspend fun invoke(email: String, password: String): Flow<DataState<String>> {
        return repository.login(email, password)
            .onEach {
                if (it is DataState.Success) {
                    settingRepository.insertToken(it.data)
                }
            }.flowOn(dispatcher)
    }

}
