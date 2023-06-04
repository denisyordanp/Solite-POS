package com.socialite.solite_pos.data.source.domain.impl

import com.socialite.solite_pos.data.source.domain.RegisterUser
import com.socialite.solite_pos.data.source.repository.AccountRepository

class RegisterUserImpl(
    private val repository: AccountRepository
) : RegisterUser {
    override suspend fun invoke(
        name: String,
        email: String,
        password: String,
        storeName: String
    ): Boolean {
        val userToken = repository.register(name, email, password, storeName)
        repository.insertToken(userToken)
        return true
    }
}
