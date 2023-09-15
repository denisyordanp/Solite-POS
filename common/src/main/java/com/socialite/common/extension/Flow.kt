package com.socialite.common.extension

import com.socialite.common.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

inline fun <T> dataStateFlow(
    dispatcher: CoroutineDispatcher,
    crossinline getData: () -> Flow<T>,
): Flow<DataState<T>> {
    return getData().map<T, DataState<T>> {
        DataState.Success(it)
    }.onStart {
        emit(DataState.Loading)
    }.catch {
        val error = (it as Exception).toError<T>()
        emit(DataState.Error(error))
    }.flowOn(dispatcher)
}