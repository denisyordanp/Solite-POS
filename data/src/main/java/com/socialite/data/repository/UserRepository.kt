package com.socialite.data.repository

import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.flow.Flow

interface UserRepository : SyncRepository<User> {
    fun getUsers(): Flow<List<User>>
    fun addUser(user: User): Flow<Boolean>
    fun updateUser(user: User): Flow<Boolean>
}
