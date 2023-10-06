package com.socialite.domain.domain.impl

import com.socialite.common.utility.di.IoDispatcher
import com.socialite.data.repository.PaymentsRepository
import com.socialite.domain.domain.GetPayments
import com.socialite.domain.helper.toDomain
import com.socialite.domain.schema.main.Payment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class GetPaymentsImpl @Inject constructor(
    private val paymentsRepository: PaymentsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : GetPayments {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(status: Payment.Status) =
        paymentsRepository.getPayments(com.socialite.schema.database.new_master.Payment.filter(status.code))
            .mapLatest { payments -> payments.map { it.toDomain() } }.flowOn(dispatcher)
}