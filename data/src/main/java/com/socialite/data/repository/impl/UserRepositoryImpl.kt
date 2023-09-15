package com.socialite.data.repository.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.database.dao.UserDao
import com.socialite.data.repository.SyncRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun getItems(): List<User> {
        return dao.getUsers().first()
    }

    override suspend fun updateItems(items: List<User>) {
        dao.updateUsers(items)
    }

    override suspend fun insertItems(items: List<User>) {
        dao.insertUsers(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<User>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override fun getUsers() = dao.getUsers().flowOn(dispatcher)

    override fun addUser(user: User) = flow {
        dao.insertUser(user)
        emit(true)
    }.flowOn(dispatcher)

    override fun updateUser(user: User) = flow {
        dao.updateUser(user)
        emit(true)
    }.flowOn(dispatcher)
}