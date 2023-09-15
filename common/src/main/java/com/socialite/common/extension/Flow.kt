package com.socialite.common.extension

import com.socialite.common.state.DataState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

inline fun <T> dataStateFlow(
    dispatcher: CoroutineDispatcher,
    getData: () -> Flow<T>,
): Flow<DataState<T>> {
    return getData().map {
        DataState.Success(it)
    }.catch {
        DataState.Error(it)
    }.onStart {
        DataState.Loading
    }.flowOn(dispatcher)
}

inline fun dataStateFlowNoData(
    dispatcher: CoroutineDispatcher,
    crossinline action: suspend () -> Unit,
): Flow<DataState<Boolean>> {
    return flow<DataState<Boolean>> {
        action()
        DataState.Success(true)
    }.catch {
        DataState.Error(it)
    }.onStart {
        DataState.Loading
    }.flowOn(dispatcher)
}