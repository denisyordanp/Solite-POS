package com.socialite.solite_pos.data.source.repository.impl

import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.source.repository.PaymentsRepository

class PaymentsRepositoryImpl(
    private val dao: PaymentsDao
) : PaymentsRepository {

    companion object {
        @Volatile
        private var INSTANCE: PaymentsRepositoryImpl? = null

        fun getInstance(
            dao: PaymentsDao
        ): PaymentsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PaymentsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PaymentsRepositoryImpl(dao = dao)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun insertPayment(data: Payment) {
        dao.insertPayment(data)
    }

    override suspend fun updatePayment(data: Payment) {
        dao.updatePayment(data)
    }

    override fun getPayments() = dao.getPayments()
}
