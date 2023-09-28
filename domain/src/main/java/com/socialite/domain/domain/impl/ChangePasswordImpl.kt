package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.common.state.DataState
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.ChangePassword
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ChangePasswordImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : ChangePassword {
    @OptIn(FlowPreview::class)
    override fun invoke(oldPassword: String, newPassword: String) = dataStateFlow(dispatcher) {
        userRepository.changePassword(oldPassword, newPassword)
    }.flatMapConcat {
        when (it) {
            is DataState.Error -> flowOf(DataState.Error(it.errorState))
            DataState.Idle -> flowOf(DataState.Idle)
            DataState.Loading -> flowOf(DataState.Loading)
            is DataState.Success -> flowOf(DataState.Success(true))
        }
    }
}