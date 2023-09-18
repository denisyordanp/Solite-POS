package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.UpdateUser
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UpdateUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UpdateUser {
    @OptIn(FlowPreview::class)
    override fun invoke(user: User) = dataStateFlow(dispatcher) {
        userRepository.updateUser(
            id = user.id,
            name = user.name,
            email = user.email,
            password = user.password,
            authority = user.authority.name,
            isActive = user.isUserActive
        )
    }.flatMapConcat<DataState<ApiResponse<String?>>, DataState<Boolean>> {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow {
                emit(DataState.Success(true))
            }
        }
    }
}