package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import com.socialite.solite_pos.utils.tools.helper.ReportParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import java.util.UUID

class OrdersRepositoryImpl(
    private val dao: OrdersDao,
    private val customersDao: CustomersDao,
    private val storesDao: StoreDao,
    private val settingRepository: SettingRepository,
    private val db: AppDatabase
) : OrdersRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrdersRepositoryImpl? = null

        fun getInstance(
            dao: OrdersDao,
            customersDao: CustomersDao,
            storesDao: StoreDao,
            settingRepository: SettingRepository,
            db: AppDatabase
        ): OrdersRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrdersRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrdersRepositoryImpl(
                            dao = dao,
                            customersDao = customersDao,
                            storesDao = storesDao,
                            settingRepository = settingRepository,
                            db = db
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override fun getOrderList(status: Int, date: String) = dao.getOrdersByStatus(status, date)

    @FlowPreview
    override fun getOrderList(status: Int, parameters: ReportParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getNewSelectedStore().flatMapConcat {
                dao.getOrdersByStatus(status, parameters.start, parameters.end, it)
            }
        } else {
            dao.getOrdersByStatus(status, parameters.start, parameters.end, parameters.storeId)
        }
    }

    override fun getOrderDataAsFlow(orderId: String) = dao.getOrderData(orderId)
    override suspend fun getNeedUploadOrders(): List<Order> = dao.getNeedUploadOrders()
    @OptIn(FlowPreview::class)
    override fun getAllOrderList(parameters: ReportParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getNewSelectedStore().flatMapConcat {
                dao.getAllOrders(parameters.start, parameters.end, it)
            }
        } else {
            dao.getAllOrders(parameters.start, parameters.end, parameters.storeId)
        }
    }
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
