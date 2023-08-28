package com.socialite.domain.domain.impl

import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.domain.domain.UpdatePayment
import javax.inject.Inject

class UpdatePaymentImpl @Inject constructor(
    private val paymentRepository: PaymentsRepository,
) : UpdatePayment {
    override suspend fun invoke(payment: Payment) {
        paymentRepository.updatePayment(payment)
    }
}