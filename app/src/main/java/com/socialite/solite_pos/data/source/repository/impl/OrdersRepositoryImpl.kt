package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import java.util.UUID

class OrdersRepositoryImpl(
    private val dao: OrdersDao,
    private val customersDao: CustomersDao,
    private val storesDao: StoreDao,
    private val promosDao: PromosDao,
    private val variantOptionsDao: VariantOptionsDao,
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
            promosDao: PromosDao,
            variantOptionsDao: VariantOptionsDao,
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
                            promosDao = promosDao,
                            variantOptionsDao = variantOptionsDao,
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
    override fun getOrderList(status: Int, date: String, store: String) =
        dao.getOrdersByStatus(status, date, store)

    @FlowPreview
    override fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>> {
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
    override suspend fun getOrderData(orderId: String): OrderData? = dao.getOrderDataById(orderId)
    override suspend fun getNeedUploadOrderPromos() = dao.getNeedUploadOrderPromos()
    override suspend fun getNeedUploadOrderProductVariants() = dao.getNeedOrderProductVariants()
    override suspend fun updateOrder(order: Order) = dao.updateNewOrder(
        order.copy(
            isUploaded = false
        )
    )

    override suspend fun insertOrder(order: Order) {
        dao.insertNewOrder(order)
    }

    override suspend fun insertOrderPromos(list: List<OrderPromo>) {
        dao.insertOrderPromos(list)
    }

    override suspend fun insertOrderProductVariants(list: List<OrderProductVariant>) {
        dao.insertOrderProductVariants(list)
    }

    override suspend fun insertOrderProductVariant(data: OrderProductVariant) {
        dao.insertNewOrderProductVariant(data)
    }

    override suspend fun getDeletedOrderProductVariantIds() = dao.getDeletedOrderProductVariantIds()

    override suspend fun deleteAllDeletedOrderProductVariants() {
        dao.deleteAllDeletedOrderProductVariants()
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

    override suspend fun insertNewPromoOrder(promo: OrderPromo) = dao.insertNewOrderPromo(promo)
    override suspend fun migrateToUUID() {
        val orders = dao.getOrders()
        val orderPromos = dao.getOrderPromos()
        val orderProductVariants = dao.getOrderProductVariants()

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

        db.withTransaction {
            for (orderPromo in orderPromos) {
                val uuid = orderPromo.new_id.ifEmpty {
                    val updatedOrderPromo = orderPromo.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderPromo(updatedOrderPromo)
                    updatedOrderPromo.new_id
                }

                val order = dao.getOrderByNo(orderPromo.orderNO)
                val promo = promosDao.getPromoById(orderPromo.idPromo)
                if (order != null && promo != null) {
                    val newOrderPromo = OrderPromo(
                        id = uuid,
                        order = order.new_id,
                        promo = promo.new_id,
                        totalPromo = orderPromo.totalPromo,
                        isUpload = orderPromo.isUpload
                    )
                    dao.insertNewOrderPromo(newOrderPromo)
                }
            }
        }

        db.withTransaction {
            for (orderProductVariant in orderProductVariants) {
                val uuid = orderProductVariant.new_id.ifEmpty {
                    val updatedOrderProductVariant = orderProductVariant.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderProductVariant(updatedOrderProductVariant)
                    updatedOrderProductVariant.new_id
                }

                val orderDetail = dao.getOrderDetailById(orderProductVariant.idOrderDetail)
                val variantOption =
                    variantOptionsDao.getVariantOptionById(orderProductVariant.idVariantOption)
                if (orderDetail != null && variantOption != null) {
                    val newOrderProductVariant = OrderProductVariant(
                        id = uuid,
                        variantOption = variantOption.new_id,
                        orderDetail = orderDetail.new_id,
                        isUpload = orderProductVariant.isUpload,
                        isDeleted = false
                    )
                    dao.insertNewOrderProductVariant(newOrderProductVariant)
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
