package com.socialite.domain.domain.impl

import com.socialite.data.repository.PaymentsRepository
import com.socialite.domain.domain.AddNewPayment
import com.socialite.domain.helper.toData
import com.socialite.schema.ui.main.Payment
import javax.inject.Inject

class AddNewPaymentImpl @Inject constructor(
  private val paymentsRepository: PaymentsRepository
) : AddNewPayment {
    override suspend fun invoke(payment: Payment) {
        paymentsRepository.insertPayment(payment.toData())
    }
}