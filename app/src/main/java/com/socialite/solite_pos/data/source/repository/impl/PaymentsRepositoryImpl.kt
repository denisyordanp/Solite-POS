package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Payment
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.source.repository.PaymentsRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Payment as NewPayment

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

    override suspend fun insertPayment(data: NewPayment) {
        dao.insertNewPayment(data)
    }

    override suspend fun updatePayment(data: NewPayment) {
        dao.updateNewPayment(data)
    }

    override fun getPayments(query: SupportSQLiteQuery) = dao.getNewPayments(query)
    override suspend fun migrateToUUID() {
        val payments = dao.getPayments(Payment.filter(Payment.ALL)).firstOrNull()
        if (!payments.isNullOrEmpty()) {
            db.withTransaction {
                for (payment in payments) {
                    val uuid = payment.new_id.ifEmpty {
                        val updatedPayment = payment.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        dao.updatePayment(updatedPayment)
                        updatedPayment.new_id
                    }

                    val newPayment = NewPayment(
                        id = uuid,
                        name = payment.name,
                        desc = payment.desc,
                        tax = payment.tax,
                        isCash = payment.isCash,
                        isActive = payment.isActive,
                        isUploaded = payment.isUploaded
                    )
                    dao.insertNewPayment(newPayment)
                }
            }
        }
    }

    override suspend fun deleteAllOldCustomers() {
        dao.deleteAllOldPayments()
    }
}
