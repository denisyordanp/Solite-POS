package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.network.response.ApiResponse
import com.socialite.common.state.DataState
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.response.UserStoreResponse
import com.socialite.domain.domain.FetchUsers
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FetchUsersImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher val dispatcher: CoroutineDispatcher
) : FetchUsers {
    @OptIn(FlowPreview::class)
    override fun invoke() = dataStateFlow(dispatcher) {
        userRepository.fetchUsers()
    }.flatMapConcat<DataState<ApiResponse<List<UserStoreResponse>>>, DataState<List<User>>> { response ->
        when (response) {
            is DataState.Error -> flowOf(DataState.Error(response.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flow {
                // save users from response
                val users = response.data.data?.map { it.toEntity() }
                userRepository.updateSynchronization(users!!)

                // get saved users
                val dataUsers = userRepository.getItems()
                emit(DataState.Success(dataUsers.map { it.toDomain() }))
            }
        }
    }
}