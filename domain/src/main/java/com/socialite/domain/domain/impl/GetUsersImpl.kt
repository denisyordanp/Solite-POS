package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.GetUsers
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUsersImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher val dispatcher: CoroutineDispatcher
) : GetUsers {
    override fun invoke() = dataStateFlow(dispatcher) {
        userRepository.getUsers().map { users -> users.map { it.toDomain() } }
    }
}