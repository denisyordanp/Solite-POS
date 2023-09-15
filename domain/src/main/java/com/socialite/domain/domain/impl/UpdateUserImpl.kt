package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.extension.dataStateFlowNoData
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.UpdateUser
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UpdateUser {
    override fun invoke(user: User) = dataStateFlowNoData(dispatcher) {
        userRepository.updateUser(user.toData())
    }
}