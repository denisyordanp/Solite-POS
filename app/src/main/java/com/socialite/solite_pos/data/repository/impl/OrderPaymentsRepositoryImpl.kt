package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.OrderPaymentsDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.repository.OrderPaymentsRepository
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import java.util.UUID
import javax.inject.Inject

class OrderPaymentsRepositoryImpl @Inject constructor(
    private val dao: OrderPaymentsDao,
    private val ordersDao: OrdersDao,
    private val paymentDao: PaymentsDao,
    private val db: AppDatabase
) : OrderPaymentsRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrderPaymentsRepositoryImpl? = null

        fun getInstance(
            dao: OrderPaymentsDao,
            ordersDao: OrdersDao,
            paymentDao: PaymentsDao,
            db: AppDatabase
        ): OrderPaymentsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrderPaymentsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrderPaymentsRepositoryImpl(
                            dao = dao,
                            ordersDao = ordersDao,
                            paymentDao = paymentDao,
                            db = db
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun getNeedUploadOrderPayments() = dao.getNeedUploadOrderPayments()

    override suspend fun insertNewPaymentOrder(payment: OrderPayment) =
        dao.insertNewOrderPayment(payment)

    override suspend fun migrateToUUID() {
        val orderPayments = dao.getOrderPayments()
        db.withTransaction {
            for (orderPayment in orderPayments) {
                val uuid = orderPayment.new_id.ifEmpty {
                    val updatedOrderPayment = orderPayment.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderPayment(updatedOrderPayment)
                    updatedOrderPayment.new_id
                }

                val order = ordersDao.getOrderByNo(orderPayment.orderNO)
                val payment = paymentDao.getPaymentById(orderPayment.idPayment)
                if (order != null && payment != null) {
                    val newOrderPayment = OrderPayment(
                        id = uuid,
                        order = order.new_id,
                        payment = payment.new_id,
                        pay = orderPayment.pay,
                        isUpload = orderPayment.isUpload
                    )
                    dao.insertNewOrderPayment(newOrderPayment)
                }
            }
        }
    }

    override suspend fun getItems(): List<OrderPayment> {
        return dao.getNewOrderPayments()
    }

    override suspend fun updateItems(items: List<OrderPayment>) {
        dao.updateNewOrderPayments(items)
    }

    override suspend fun insertItems(items: List<OrderPayment>) {
        dao.insertOrderPayments(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<OrderPayment>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }
}
