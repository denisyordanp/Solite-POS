package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.DefaultDispatcher
import com.socialite.data.repository.StoreRepository
import com.socialite.domain.domain.GetSelectedStore
import com.socialite.domain.domain.GetSelectedStoreId
import com.socialite.domain.helper.toDomain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetSelectedStoreImpl @Inject constructor(
    private val getSelectedStoreId: GetSelectedStoreId,
    private val storeRepository: StoreRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) : GetSelectedStore {
    @OptIn(FlowPreview::class)
    override fun invoke() = getSelectedStoreId().flatMapConcat { storeId ->
        flow {
            val store = storeRepository.getStore(storeId)?.toDomain()

            emit(store)
        }
    }.flowOn(dispatcher)
}