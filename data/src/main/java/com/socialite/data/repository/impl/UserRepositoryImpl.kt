package com.socialite.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.socialite.common.di.IoDispatcher
import com.socialite.common.extension.toResponse
import com.socialite.common.network.NetworkConfig
import com.socialite.data.database.dao.UserDao
import com.socialite.data.di.AuthorizationService
import com.socialite.data.network.SoliteServices
import com.socialite.data.repository.SyncRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.master.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @AuthorizationService private val service: SoliteServices,
    private val dao: UserDao,
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserRepository {

    private object PreferencesKeys {
        val LOGGED_IN_USER = stringPreferencesKey("logged_in_user")
    }

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

    override fun getUsers() = flow {
        val response = service.getUsers()
        emit(response)
    }.flowOn(dispatcher)

    override fun addUser(user: User) = flow {
        dao.insertUser(user)
        emit(true)
    }.flowOn(dispatcher)

    override fun postNewUserUser(
        name: String,
        email: String,
        password: String,
        authority: String
    ) = flow {
        val request = service.addUser(
            name = name,
            email = email,
            password = password,
            authority = authority
        )

        emit(request)
    }.flowOn(dispatcher)

    override fun updateUser(
        id: String,
        name: String,
        email: String,
        password: String,
        authority: String,
        isActive: Boolean
    ) = flow {
        val request = service.updateUser(id, name, email, authority, isActive.toResponse())
        emit(request)
    }.flowOn(dispatcher)

    override fun saveLoggedInUser(user: User?) = flow {
        user?.let {
            val jsonUser = NetworkConfig.gson().toJson(user)
            dataStore.edit {
                it[PreferencesKeys.LOGGED_IN_USER] = jsonUser
            }
        }
        emit(true)
    }.flowOn(dispatcher)

    override fun getLoggedInUser() = dataStore.data.map {
        val jsonUser = it[PreferencesKeys.LOGGED_IN_USER]
        jsonUser?.let {
            NetworkConfig.gson().fromJson(jsonUser, User::class.java)
        }
    }.flowOn(dispatcher)
}