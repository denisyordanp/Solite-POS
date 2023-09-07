package com.socialite.domain.domain.impl

import com.socialite.data.repository.PromosRepository
import com.socialite.domain.domain.GetPromos
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.Promo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import com.socialite.data.schema.room.new_master.Promo as DataPromo

class GetPromosImpl @Inject constructor(
    private val promosRepository: PromosRepository
) : GetPromos {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Promo.Status) =
        promosRepository.getPromos(DataPromo.filter(status.code))
            .mapLatest { promos -> promos.map { it.toDomain() } }
}