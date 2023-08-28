package com.socialite.domain.domain.impl

import com.socialite.data.repository.PromosRepository
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.domain.domain.UpdatePromo
import javax.inject.Inject

class UpdatePromoImpl @Inject constructor(
    private val promosRepository: PromosRepository
) : UpdatePromo {
    override suspend fun invoke(promo: Promo) {
        promosRepository.updatePromo(promo)
    }
}