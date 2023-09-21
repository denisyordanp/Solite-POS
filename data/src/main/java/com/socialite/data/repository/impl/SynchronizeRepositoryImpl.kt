package com.socialite.data.repository.impl

import com.socialite.common.di.IoDispatcher
import com.socialite.data.di.AuthorizationService
import com.socialite.data.network.SoliteServices
import com.socialite.data.repository.SynchronizeRepository
import com.socialite.data.schema.response.SynchronizeParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SynchronizeRepositoryImpl @Inject constructor(
    @AuthorizationService private val service: SoliteServices,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : SynchronizeRepository {
    override fun synchronize(param: SynchronizeParams) = flow {
        val request = service.synchronize(param)

        emit(request)
    }.flowOn(dispatcher)
}