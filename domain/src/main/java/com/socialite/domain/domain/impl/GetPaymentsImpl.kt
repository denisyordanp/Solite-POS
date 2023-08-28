package com.socialite.domain.domain.impl

import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.data.repository.PaymentsRepository
import com.socialite.domain.domain.GetPayments
import javax.inject.Inject

class GetPaymentsImpl @Inject constructor(
    private val paymentsRepository: PaymentsRepository
) : GetPayments {
    override fun invoke(query: SupportSQLiteQuery) = paymentsRepository.getPayments(query)
}