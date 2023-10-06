package com.socialite.data.repository

import com.socialite.core.network.response.ApiResponse
import com.socialite.data.schema.response.UserResponse
import com.socialite.data.schema.response.UserStoreResponse
import com.socialite.schema.database.master.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : SyncRepository<User> {
    fun fetchUsers(): Flow<ApiResponse<List<UserStoreResponse>>>
    fun getUsers(): Flow<List<User>>
    fun fetchUser(): Flow<ApiResponse<UserStoreResponse>>
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
    fun changePassword(oldPassword: String, newPassword: String): Flow<ApiResponse<String?>>
}
