package com.socialite.data.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    fun <T> saveData(key: Preferences.Key<T>, data: T): Flow<Boolean>
    fun <T> saveMapData(key: Preferences.Key<T>, data: () -> T): Flow<Boolean>
    fun <T> getData(key: Preferences.Key<T>, default: T): Flow<T>
    fun <T, R> getMapData(key: Preferences.Key<T>, map: (T?) -> R): Flow<R>
}