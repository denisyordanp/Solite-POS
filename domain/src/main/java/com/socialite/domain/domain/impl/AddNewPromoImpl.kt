package com.socialite.domain.domain.impl

import com.socialite.data.repository.PromosRepository
import com.socialite.data.schema.room.new_master.Promo
import com.socialite.domain.domain.AddNewPromo
import javax.inject.Inject

class AddNewPromoImpl @Inject constructor(
    private val promosRepository: PromosRepository,
) : AddNewPromo {
    override suspend fun invoke(promo: Promo) {
        promosRepository.insertPromo(promo)
    }
}