package com.socialite.domain.domain.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.common.extension.dataStateFlowNoData
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.AddNewUser
import com.socialite.domain.helper.IdManager
import com.socialite.domain.helper.toNewUserData
import com.socialite.domain.schema.main.User
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddNewUserImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val idManager: IdManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AddNewUser {
    override fun invoke(user: User) = dataStateFlowNoData(dispatcher) {
        userRepository.addUser(user.toNewUserData(idManager.generateNewId()))
    }
}