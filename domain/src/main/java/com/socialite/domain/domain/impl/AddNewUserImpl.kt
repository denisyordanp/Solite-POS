package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.response.UserResponse
import com.socialite.domain.domain.AddNewUser
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AddNewUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : AddNewUser {
    @OptIn(FlowPreview::class)
    override fun invoke(user: User) = dataStateFlow(dispatcher) {
        userRepository.postNewUserUser(
            name = user.name,
            email = user.email,
            password = user.password,
            authority = user.authority.name
        )
    }.flatMapConcat<DataState<ApiResponse<UserResponse>>, DataState<Boolean>> {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow {
                val isUserAdded = it.data.data?.let { response ->
                    userRepository.addUser(response.toUser()).first()
                } ?: false

                emit(DataState.Success(isUserAdded))
            }
        }
    }
}