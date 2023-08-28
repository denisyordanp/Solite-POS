package com.socialite.domain.domain.impl

import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.schema.room.new_master.Payment
import com.socialite.domain.domain.AddNewPayment
import javax.inject.Inject

class AddNewPaymentImpl @Inject constructor(
  private val paymentsRepository: PaymentsRepository
) : AddNewPayment {
    override suspend fun invoke(payment: Payment) {
        paymentsRepository.insertPayment(payment)
    }
}