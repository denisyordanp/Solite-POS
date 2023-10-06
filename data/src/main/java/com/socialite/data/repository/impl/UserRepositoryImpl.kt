package com.socialite.data.repository.impl

import androidx.datastore.preferences.core.stringPreferencesKey
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.extensions.toResponse
import com.socialite.core.network.NetworkConfig
import com.socialite.core.database.dao.UserDao
import com.socialite.data.datastore.DataStoreManager
import com.socialite.data.di.AuthorizationService
import com.socialite.data.network.SoliteServices
import com.socialite.data.repository.SyncRepository
import com.socialite.data.repository.UserRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.master.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @AuthorizationService private val service: SoliteServices,
    private val dao: UserDao,
    private val dataStoreManager: DataStoreManager,
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

    override fun fetchUsers() = flow {
        val response = service.getUsers()
        emit(response)
    }.flowOn(dispatcher)

    override fun getUsers() = dao.getUsers().flowOn(dispatcher)

    override fun fetchUser() = flow {
        val response = service.getUser()
        emit(response)
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

    override fun saveLoggedInUser(user: User?) =
        dataStoreManager.saveMapData(PreferencesKeys.LOGGED_IN_USER) {
            user?.let {
                NetworkConfig.gson().toJson(user)
            } ?: ""
        }.flowOn(dispatcher)

    override fun getLoggedInUser() =
        dataStoreManager.getMapData(PreferencesKeys.LOGGED_IN_USER) { jsonUser ->
            jsonUser.let {
                NetworkConfig.gson().fromJson(it, User::class.java)
            }
        }.flowOn(dispatcher)

    override fun changePassword(
        oldPassword: String,
        newPassword: String
    ) = flow {
        val request = service.changePassword(oldPassword, newPassword)
        emit(request)
    }.flowOn(dispatcher)
}