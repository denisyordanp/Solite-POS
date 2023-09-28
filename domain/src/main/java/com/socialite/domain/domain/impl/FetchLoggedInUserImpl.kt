package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.response.UserStoreResponse
import com.socialite.domain.domain.FetchLoggedInUser
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FetchLoggedInUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : FetchLoggedInUser {
    @OptIn(FlowPreview::class)
    override fun invoke() = dataStateFlow(dispatcher) {
        userRepository.fetchUser()
    }.flatMapConcat<DataState<ApiResponse<UserStoreResponse>>, DataState<User>> {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow {
                val response = it.data.data
                val userData = response?.toEntity()
                userRepository.saveLoggedInUser(userData).collect()
                emit(DataState.Success(userData!!.toDomain()))
            }
        }
    }
}