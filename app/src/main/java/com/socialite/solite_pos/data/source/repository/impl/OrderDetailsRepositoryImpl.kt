package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.OrderDetailsDao
import com.socialite.solite_pos.data.source.local.room.OrderProductVariantsDao
import com.socialite.solite_pos.data.source.local.room.OrdersDao
import com.socialite.solite_pos.data.source.local.room.ProductsDao
import com.socialite.solite_pos.data.source.repository.OrderDetailsRepository
import com.socialite.solite_pos.data.source.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import java.util.UUID
import javax.inject.Inject

class OrderDetailsRepositoryImpl @Inject constructor(
    private val dao: OrderDetailsDao,
    private val ordersDao: OrdersDao,
    private val orderProductVariantsDao: OrderProductVariantsDao,
    private val productsDao: ProductsDao,
    private val db: AppDatabase
) : OrderDetailsRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrderDetailsRepositoryImpl? = null

        fun getInstance(
            dao: OrderDetailsDao,
            ordersDao: OrdersDao,
            orderProductVariantsDao: OrderProductVariantsDao,
            productsDao: ProductsDao,
            db: AppDatabase
        ): OrderDetailsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrderDetailsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrderDetailsRepositoryImpl(
                            dao = dao,
                            ordersDao = ordersDao,
                            orderProductVariantsDao = orderProductVariantsDao,
                            productsDao = productsDao,
                            db = db
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun getNeedUploadOrderDetails() = dao.getNeedUploadOrderDetails()

    override suspend fun insertOrderDetail(orderDetail: OrderDetail) {
        dao.insertNewOrderDetail(orderDetail)
    }

    override suspend fun getDeletedOrderDetailIds() = dao.getDeletedOrderDetailIds()
    override fun getOrderDetailByIdOrder(orderId: String) = dao.getOrderDetailByIdOrder(orderId)
    override fun getOrderDetail() = dao.getOrderDetailsFlow()
    override suspend fun getOrderDetailWithVariants(idDetail: String) =
        dao.getOrderDetailWithVariants(idDetail)

    override suspend fun deleteOrderDetailAndRelated(orderId: String) {
        val orderDetails = dao.getNewOrderDetailsByOrderId(orderId)
        orderDetails.forEach {
            dao.updateNewOrderDetail(
                it.copy(
                    isDeleted = true
                )
            )
            orderProductVariantsDao.updateOrderProductVariantsByDetailId(it.id)
        }
    }

    override suspend fun deleteAllDeletedOrderDetails() {
        dao.deleteAllDeletedOrderDetails()
    }

    override suspend fun getItems(): List<OrderDetail> {
        return dao.getNewOrderDetails()
    }

    override suspend fun updateItems(items: List<OrderDetail>) {
        dao.updateOrderDetails(items)
    }

    override suspend fun insertItems(items: List<OrderDetail>) {
        dao.insertOrderDetails(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<OrderDetail>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun migrateToUUID() {
        val orderDetails = dao.getOrderDetails()
        db.withTransaction {
            for (orderDetail in orderDetails) {
                val uuid = orderDetail.new_id.ifEmpty {
                    val updatedOrderDetail = orderDetail.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderDetail(updatedOrderDetail)
                    updatedOrderDetail.new_id
                }

                val order = ordersDao.getOrderByNo(orderDetail.orderNo)
                val product = productsDao.getProductById(orderDetail.idProduct)
                if (order != null && product != null) {
                    val newOrderDetail = OrderDetail(
                        id = uuid,
                        order = order.new_id,
                        product = product.new_id,
                        amount = orderDetail.amount,
                        isUpload = orderDetail.isUpload,
                        isDeleted = false
                    )
                    dao.insertNewOrderDetail(newOrderDetail)
                }
            }
        }
    }
}
