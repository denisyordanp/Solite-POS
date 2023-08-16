package com.socialite.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.PaymentsDao
import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.master.Payment
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import com.socialite.data.schema.room.new_master.Payment as NewPayment

class PaymentsRepositoryImpl @Inject  constructor(
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
        dao.updateNewPayment(data.copy(
            isUploaded = false
        ))
    }

    override fun getPayments(query: SupportSQLiteQuery) = dao.getNewPayments(query)
    override suspend fun getNeedUploadPayments() = dao.getNeedUploadPayments()

    override suspend fun getItems(): List<NewPayment> {
        return dao.getNewPayments(NewPayment.filter(NewPayment.ALL)).first()
    }

    override suspend fun updateItems(items: List<NewPayment>) {
        dao.updatePayments(items)
    }

    override suspend fun insertItems(items: List<NewPayment>) {
        dao.insertPayments(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<NewPayment>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

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

    override suspend fun deleteAllOldPayments() {
        dao.deleteAllOldPayments()
    }

    override suspend fun deleteAllNewPayments() {
        dao.deleteAllNewPayments()
    }
}
