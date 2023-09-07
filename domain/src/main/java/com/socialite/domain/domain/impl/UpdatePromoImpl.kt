package com.socialite.domain.domain.impl

import com.socialite.data.repository.PromosRepository
import com.socialite.domain.domain.UpdatePromo
import com.socialite.domain.helper.toData
import com.socialite.domain.schema.main.Promo
import javax.inject.Inject

class UpdatePromoImpl @Inject constructor(
    private val promosRepository: PromosRepository
) : UpdatePromo {
    override suspend fun invoke(promo: Promo) {
        promosRepository.updatePromo(promo.toData())
    }
}