package com.socialite.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM ${User.DB_NAME}")
    fun getUsers(): Flow<List<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(data: User)

    @Insert
    suspend fun insertUsers(users: List<User>)

    @Update
    suspend fun updateUsers(data: List<User>)
}
