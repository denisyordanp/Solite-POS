package com.socialite.domain.domain.impl

import com.socialite.data.repository.PromosRepository
import com.socialite.domain.domain.AddNewPromo
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Promo
import javax.inject.Inject

class AddNewPromoImpl @Inject constructor(
    private val promosRepository: PromosRepository,
) : AddNewPromo {
    override suspend fun invoke(promo: Promo) {
        promosRepository.insertPromo(promo.toData())
    }
}