package com.socialite.domain.domain.impl

import com.socialite.data.repository.PaymentsRepository
import com.socialite.domain.domain.UpdatePayment
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Payment
import javax.inject.Inject

class UpdatePaymentImpl @Inject constructor(
    private val paymentRepository: PaymentsRepository,
) : UpdatePayment {
    override suspend fun invoke(payment: Payment) {
        paymentRepository.updatePayment(payment.toData())
    }
}