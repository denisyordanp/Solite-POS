package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow

interface OrdersRepository : SyncRepository<Order> {

    fun getOrderList(status: Int, date: String): Flow<List<OrderData>>
    fun getOrderList(status: Int, date: String, store: String): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>>
    fun getOrderData(orderId: String): Flow<OrderData?>
    suspend fun getOrderDetail(orderId: String): OrderData?
    suspend fun getNeedUploadOrderDetails(): List<OrderDetail>
    suspend fun getNeedUploadOrderPayments(): List<OrderPayment>
    suspend fun getNeedUploadOrderPromos(): List<OrderPromo>
    suspend fun getNeedUploadOrderProductVariants(): List<OrderProductVariant>
    suspend fun updateOrder(order: Order)
    suspend fun insertOrders(list: List<Order>)
    suspend fun insertOrderPayments(list: List<OrderPayment>)
    suspend fun insertOrderDetails(list: List<OrderDetail>)
    suspend fun insertOrderDetail(orderDetail: OrderDetail)
    suspend fun getDeletedOrderDetailIds(): List<String>
    suspend fun deleteOrderDetailAndRelated(orderId: String)
    suspend fun insertNewPaymentOrder(payment: OrderPayment)
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
    suspend fun insertOrderPromos(list: List<OrderPromo>)
    suspend fun insertOrderProductVariants(list: List<OrderProductVariant>)
    suspend fun getDeletedOrderProductVariantIds(): List<String>
    suspend fun deleteAllDeletedOrderDetails()
    suspend fun deleteAllDeletedOrderProductVariants()
}
