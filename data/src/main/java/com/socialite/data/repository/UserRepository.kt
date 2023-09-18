package com.socialite.data.repository

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.LoginResponse
import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : SyncRepository<User> {
    fun getUsers(): Flow<List<User>>
    fun addUser(user: User): Flow<Boolean>
    fun postNewUserUser(
        name: String,
        email: String,
        password: String,
        authority: String
    ): Flow<ApiResponse<LoginResponse>>

    fun updateUser(user: User): Flow<Boolean>
    fun saveLoggedInUser(user: User?): Flow<Boolean>
    fun getLoggedInUser(): Flow<User?>
}
