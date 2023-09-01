package com.socialite.domain.domain.impl

import com.socialite.data.repository.PaymentsRepository
import com.socialite.domain.domain.GetPayments
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.Payment
import com.socialite.data.schema.room.new_master.Payment as DataPayment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetPaymentsImpl @Inject constructor(
    private val paymentsRepository: PaymentsRepository
) : GetPayments {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Payment.Status) = paymentsRepository.getPayments(DataPayment.filter(status.code))
        .mapLatest { payments -> payments.map { it.toDomain() } }
}