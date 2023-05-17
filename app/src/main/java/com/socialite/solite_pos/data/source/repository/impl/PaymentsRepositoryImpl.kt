package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class PaymentsRepositoryImpl(
    private val dao: PaymentsDao,
    private val db: AppDatabase
) : PaymentsRepository {

    companion object {
        @Volatile
        private var INSTANCE: PaymentsRepositoryImpl? = null

        fun getInstance(
            dao: PaymentsDao,
            db: AppDatabase
        ): PaymentsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PaymentsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PaymentsRepositoryImpl(dao = dao, db = db)
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

    override fun getPayments(query: SupportSQLiteQuery) = dao.getPayments(query)
    override suspend fun migrateToUUID() {
        val payments = dao.getPayments(Payment.filter(Payment.ALL)).firstOrNull()
        if (!payments.isNullOrEmpty()) {
            db.withTransaction {
                for (payment in payments) {
                    dao.updatePayment(payment.copy(
                        new_id = UUID.randomUUID().toString()
                    ))
                }
            }
        }
    }
}
