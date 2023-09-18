package com.socialite.data.repository

import com.socialite.common.network.response.ApiResponse
import com.socialite.data.schema.response.UserResponse
import com.socialite.data.schema.response.UserStoreResponse
import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : SyncRepository<User> {
    fun getUsers(): Flow<ApiResponse<List<UserStoreResponse>>>
    fun addUser(user: User): Flow<Boolean>
    fun postNewUserUser(
        name: String,
        email: String,
        password: String,
        authority: String
    ): Flow<ApiResponse<UserResponse>>

    fun updateUser(
        id: String,
        name: String,
        email: String,
        password: String,
        authority: String,
        isActive: Boolean
    ): Flow<ApiResponse<String?>>

    fun saveLoggedInUser(user: User?): Flow<Boolean>
    fun getLoggedInUser(): Flow<User?>
}
