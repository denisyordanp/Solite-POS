package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.PromosRepository
import com.socialite.domain.domain.GetPromos
import com.socialite.domain.helper.toDomain
import com.socialite.schema.ui.main.Promo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetPromosImpl @Inject constructor(
    private val promosRepository: PromosRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetPromos {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Promo.Status) =
        promosRepository.getPromos(com.socialite.schema.database.new_master.Promo.filter(status.code))
            .mapLatest { promos -> promos.map { it.toDomain() } }.flowOn(dispatcher)
}