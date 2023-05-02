package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.LoginUser
import com.socialite.solite_pos.data.source.repository.UserRepository

class LoginUserImpl(
    private val repository: UserRepository
) : LoginUser {
    override suspend fun invoke(email: String, password: String): Boolean {
        val userToken = repository.login(email, password)
        repository.insertToken(userToken)
        return true
    }

}
