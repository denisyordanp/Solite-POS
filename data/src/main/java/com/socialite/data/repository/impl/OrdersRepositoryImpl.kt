package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.common.di.IoDispatcher
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.CustomersDao
import com.socialite.data.database.dao.OrdersDao
import com.socialite.data.database.dao.StoreDao
import com.socialite.data.repository.OrdersRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_master.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val dao: OrdersDao,
    private val customersDao: CustomersDao,
    private val storesDao: StoreDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : OrdersRepository {

    override fun getOrderList(status: Int, storeId: String, date: String) =
        dao.getOrdersByStatus(status, storeId, date).flowOn(dispatcher)

    override fun getOrderList(
        status: Int,
        from: String,
        until: String,
        store: String
    ) = dao.getOrdersByStatus(status, from, until, store).flowOn(dispatcher)

    override fun getOrderList(from: String, until: String, store: String) =
        dao.getOrdersByStatus(from, until, store)

    override fun getOrderDataAsFlow(orderId: String) = dao.getOrderData(orderId).flowOn(dispatcher)
    override suspend fun getNeedUploadOrders(): List<Order> = dao.getNeedUploadOrders()

    override suspend fun updateOrder(order: Order) = dao.updateNewOrder(
        order.copy(
            isUploaded = false
        )
    )

    override suspend fun insertOrder(order: Order) {
        dao.insertNewOrder(order)
    }

    override suspend fun getItems(): List<Order> {
        return dao.getNewOrders()
    }

    override suspend fun updateItems(items: List<Order>) {
        dao.updateOrders(items)
    }

    override suspend fun insertItems(items: List<Order>) {
        dao.insertOrders(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<Order>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun migrateToUUID() {
        val orders = dao.getOrders()
        db.withTransaction {
            for (order in orders) {
                val uuid = order.new_id.ifEmpty {
                    val updatedOrder = order.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrder(updatedOrder)
                    updatedOrder.new_id
                }

                val customer = customersDao.getCustomerById(order.customer)
                val store = storesDao.getStore(order.store)
                if (customer != null && store != null) {
                    val newOrder = Order(
                        id = uuid,
                        orderNo = order.orderNo,
                        customer = customer.new_id,
                        orderTime = order.orderTime,
                        isTakeAway = order.isTakeAway,
                        status = order.status,
                        store = store.new_id,
                        isUploaded = order.isUploaded
                    )
                    dao.insertNewOrder(newOrder)
                }
            }
        }
    }

    override suspend fun deleteAllOldOrders() {
        dao.deleteAllOldOrders()
        dao.deleteAllOldOrderDetails()
        dao.deleteAllOldOrderPayments()
        dao.deleteAllOldOrderPromos()
        dao.deleteAllOldOrderProductVariants()
    }

    override suspend fun deleteAllNewOrders() {
        dao.deleteAllNewOrders()
        dao.deleteAllNewOrderDetails()
        dao.deleteAllNewOrderPayments()
        dao.deleteAllNewOrderPromos()
        dao.deleteAllNewOrderProductVariants()
    }
}
