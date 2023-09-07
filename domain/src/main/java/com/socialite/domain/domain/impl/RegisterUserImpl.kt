package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.state.DataState
import com.socialite.domain.domain.RegisterUser
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class RegisterUserImpl @Inject constructor(
    private val repository: AccountRepository,
    private val settingRepository: SettingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : RegisterUser {
    override suspend fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Flow<DataState<String>> {
        return repository.register(name, email, password, storeName)
            .onEach {
                if (it is DataState.Success) {
                    settingRepository.insertToken(it.data)
                }
            }.flowOn(dispatcher)
    }
}
