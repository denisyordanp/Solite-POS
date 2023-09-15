package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.repository.AccountRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.response.LoginResponse
import com.socialite.domain.domain.LoginUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUserImpl @Inject constructor(
    private val repository: AccountRepository,
    private val userRepository: UserRepository,
    private val settingRepository: SettingRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : LoginUser {
    @OptIn(FlowPreview::class)
    override fun invoke(email: String, password: String): Flow<DataState<Boolean>> {
        return dataStateFlow(dispatcher) {
            repository.login(email, password)
        }.flatMapConcat<DataState<ApiResponse<LoginResponse>>, DataState<Boolean>> {
            when (it) {
                is DataState.Error -> flowOf(DataState.Error(it.errorState))
                DataState.Idle -> flowOf(DataState.Idle)
                DataState.Loading -> flowOf(DataState.Loading)
                is DataState.Success -> flow {
                    val response = it.data.data
                    val savedLogin = userRepository.saveLoggedInUser(response?.toUser()).first()
                    settingRepository.insertToken(response?.token.orEmpty())

                    emit(DataState.Success(savedLogin))
                }
            }
        }.flowOn(dispatcher)
    }

}
