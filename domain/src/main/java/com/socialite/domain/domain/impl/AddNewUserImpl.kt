package com.socialite.domain.domain.impl

import com.socialite.common.di.DefaultDispatcher
import com.socialite.common.extension.dataStateFlow
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
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : AddNewUser {
    override fun invoke(user: User) = dataStateFlow(dispatcher) {
        userRepository.addUser(user.toNewUserData(idManager.generateNewId()))
    }
}