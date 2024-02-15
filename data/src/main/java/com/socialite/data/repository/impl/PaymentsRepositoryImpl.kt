package com.socialite.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.database.AppDatabase
import com.socialite.core.database.dao.PaymentsDao
import com.socialite.data.repository.PaymentsRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.master.Payment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
import com.socialite.schema.database.new_master.Payment as NewPayment

class PaymentsRepositoryImpl @Inject constructor(
    private val dao: PaymentsDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PaymentsRepository {

    override suspend fun insertPayment(data: NewPayment) {
        dao.insertNewPayment(data)
    }

    override suspend fun updatePayment(data: NewPayment) {
        dao.updateNewPayment(
            data.copy(
                isUploaded = false
            )
        )
    }

    override fun getPayments(query: SupportSQLiteQuery) =
        dao.getNewPayments(query).flowOn(dispatcher)

    override suspend fun getNeedUploadPayments() = dao.getNeedUploadPayments()

    override suspend fun getItems(): List<NewPayment> {
        return dao.getNewPayments(com.socialite.schema.database.new_master.Payment.filter(com.socialite.schema.database.new_master.Payment.ALL)).first()
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
