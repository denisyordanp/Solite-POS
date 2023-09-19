package com.socialite.data.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.socialite.data.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl constructor(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher
) : DataStoreManager {
    override fun <T> saveData(key: Preferences.Key<T>, data: T) = flow {
        dataStore.edit {
            it[key] = data
        }
        emit(true)
    }.flowOn(dispatcher)

    override fun <T> saveMapData(key: Preferences.Key<T>, data: () -> T) = flow {
        dataStore.edit {
            it[key] = data()
        }
        emit(true)
    }.flowOn(dispatcher)

    override fun <T> getData(key: Preferences.Key<T>, default: T) = dataStore.data.map {
        it[key] ?: default
    }.flowOn(dispatcher)

    override fun <T, R> getMapData(key: Preferences.Key<T>, map: (T?) -> R) = dataStore.data.map {
        map(it[key])
    }.flowOn(dispatcher)
}