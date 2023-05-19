package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order as NewOrder
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.CustomersDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.PaymentsDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.local.room.StoreDao
import com.socialite.solite_pos.data.source.repository.OrdersRepository
import com.socialite.solite_pos.data.source.repository.SettingRepository
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import java.util.UUID

class OrdersRepositoryImpl(
    private val dao: OrdersDao,
    private val customersDao: CustomersDao,
    private val storesDao: StoreDao,
    private val productsDao: ProductsDao,
    private val paymentDao: PaymentsDao,
    private val promosDao: PromosDao,
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
            productsDao: ProductsDao,
            paymentDao: PaymentsDao,
            promosDao: PromosDao,
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
                            productsDao = productsDao,
                            paymentDao = paymentDao,
                            promosDao = promosDao,
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
    override fun getOrderList(status: Int, date: String, store: Long) =
        dao.getOrdersByStatus(status, date, store)

    @FlowPreview
    override fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>> {
        return if (parameters.isTodayOnly()) {
            settingRepository.getSelectedStore().flatMapConcat {
                dao.getOrdersByStatus(status, parameters.start, parameters.end, it)
            }
        } else {
            dao.getOrdersByStatus(status, parameters.start, parameters.end, parameters.storeId)
        }
    }

    override fun getOrderData(orderNo: String) = dao.getOrderData(orderNo)

    override suspend fun getOrderDetail(orderNo: String): OrderData? = dao.getOrderDataByNo(orderNo)

    override suspend fun updateOrder(order: Order) = dao.updateOrder(order)

    override suspend fun insertPaymentOrder(payment: OrderPayment): OrderPayment {
        val id = dao.insertPaymentOrder(payment)
        payment.id = id
        return payment
    }

    override suspend fun insertNewPaymentOrder(payment: OrderPayment) =
        dao.insertNewPaymentOrder(payment)

    override suspend fun insertNewPromoOrder(promo: OrderPromo) = dao.insertNewPromoOrder(promo)
    override suspend fun migrateToUUID() {
        val orders = dao.getOrders()
        val orderDetails = dao.getOrderDetails()
        val orderPayments = dao.getOrderPayments()
        val orderPromos = dao.getOrderPromos()
        val orderProductVariants = dao.getOrderProductVariants()

        db.withTransaction {
            for (order in orders) {
                val updatedOrder = order.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateOrder(updatedOrder)

                val customer = customersDao.getCustomerById(order.customer)
                val store = storesDao.getStore(order.store)
                if (customer != null && store != null) {
                    val newOrder = NewOrder(
                        id = updatedOrder.new_id,
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

            for (orderDetail in orderDetails) {
                val updatedOrderDetail = orderDetail.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateOrderDetail(updatedOrderDetail)

                val order = dao.getOrderByNo(orderDetail.orderNo)
                val product = productsDao.getProductById(orderDetail.idProduct)
                if (order != null && product != null) {
                    val newOrderDetail = NewOrderDetail(
                        id = updatedOrderDetail.new_id,
                        order = order.new_id,
                        product = product.new_id,
                        amount = orderDetail.amount,
                        isUpload = orderDetail.isUpload
                    )
                    dao.insertNewOrderDetail(newOrderDetail)
                }
            }

            for (orderPayment in orderPayments) {
                val updatedOrderPayment = orderPayment.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateOrderPayment(updatedOrderPayment)

                val order = dao.getOrderByNo(orderPayment.orderNO)
                val payment = paymentDao.getPaymentById(orderPayment.idPayment)
                if (order != null && payment != null) {
                    val newOrderPayment = NewOrderPayment(
                        id = updatedOrderPayment.new_id,
                        order = order.new_id,
                        payment = payment.new_id,
                        pay = orderPayment.pay,
                        isUpload = orderPayment.isUpload
                    )
                    dao.insertNewOrderPayment(newOrderPayment)
                }
            }

            for (orderPromo in orderPromos) {
                val updatedOrderPromo = orderPromo.copy(
                    new_id = UUID.randomUUID().toString()
                )
                dao.updateOrderPromo(updatedOrderPromo)

                val order = dao.getOrderByNo(orderPromo.orderNO)
                val promo = promosDao.getPromoById(orderPromo.idPromo)
                if (order != null && promo != null) {
                    val newOrderPromo = NewOrderPromo(
                        id = updatedOrderPromo.new_id,
                        order = order.new_id,
                        promo = promo.new_id,
                        totalPromo = orderPromo.totalPromo,
                        isUpload = orderPromo.isUpload
                    )
                    dao.insertNewOrderPromo(newOrderPromo)
                }
            }

            for (orderProductVariant in orderProductVariants) {
                dao.updateOrderProductVariant(orderProductVariant.copy(
                    new_id = UUID.randomUUID().toString()
                ))
            }
        }
    }
}
