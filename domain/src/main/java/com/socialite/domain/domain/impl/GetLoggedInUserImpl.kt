package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.GetLoggedInUser
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLoggedInUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : GetLoggedInUser {
    override fun invoke() = dataStateFlow(dispatcher) {
        userRepository.getLoggedInUser().map { it?.toDomain() }
    }
}