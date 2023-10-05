package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.extensions.toLongDefault
import com.socialite.data.repository.OutcomesRepository
import com.socialite.data.repository.SettingRepository
import com.socialite.data.repository.UserRepository
import com.socialite.domain.domain.GetOutcomes
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.Outcome
import com.socialite.domain.schema.ReportParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetOutcomesImpl @Inject constructor(
    private val outcomesRepository: OutcomesRepository,
    private val settingRepository: SettingRepository,
    private val userRepository: UserRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetOutcomes {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun invoke(parameters: ReportParameter): Flow<List<Outcome>> {
        return flow {
            if (parameters.isTodayOnly()) {
                emitAll(
                    settingRepository.getNewSelectedStore().combine(
                        userRepository.getLoggedInUser()
                    ) { store, user ->
                        Pair(store, user)
                    }.flatMapConcat {
                        outcomesRepository.getOutcomes(
                            parameters.start,
                            parameters.end,
                            it.first,
                            it.second?.id?.toLong() ?: 0L
                        )
                    }
                )
            } else if (parameters.isLoggedInUserOnly()) {
                emitAll(
                    userRepository.getLoggedInUser().flatMapConcat {
                        outcomesRepository.getOutcomes(
                            parameters.start,
                            parameters.end,
                            parameters.storeId,
                            it?.id?.toLongDefault(0L) ?: 0L
                        )
                    }
                )
            } else {
                if (parameters.isAllStoreAndUser()) {
                    emitAll(
                        outcomesRepository.getOutcomesAllStoreAndUser(
                            from = parameters.start,
                            until = parameters.end
                        )
                    )
                } else if (parameters.isAllStore()) {
                    emitAll(
                        outcomesRepository.getOutcomesAllStore(
                            parameters.start,
                            parameters.end,
                            parameters.userId
                        )
                    )
                } else if (parameters.isAllUser()) {
                    emitAll(
                        outcomesRepository.getOutcomesAllUser(
                            parameters.start,
                            parameters.end,
                            parameters.storeId
                        )
                    )
                } else {
                    emitAll(
                        outcomesRepository.getOutcomes(
                            parameters.start,
                            parameters.end,
                            parameters.storeId,
                            parameters.userId
                        )
                    )
                }
            }
        }.mapLatest { outcomes ->
            outcomes.map { it.toDomain() }
        }.flowOn(dispatcher)
    }
}