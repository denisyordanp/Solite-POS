package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order
import com.socialite.solite_pos.utils.tools.helper.ReportsParameter
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {

    fun getOrderList(status: Int, date: String): Flow<List<OrderData>>
    fun getOrderList(status: Int, date: String, store: String): Flow<List<OrderData>>
    suspend fun getNeedUploadOrders(): List<Order>
    fun getOrderList(status: Int, date: String, store: Long): Flow<List<OrderData>>
    fun getOrderList(status: Int, parameters: ReportsParameter): Flow<List<OrderData>>
    fun getOrderData(orderId: String): Flow<OrderData?>
    suspend fun getOrderDetail(orderId: String): OrderData?
    suspend fun getNeedUploadOrderDetails(): List<OrderDetail>
    suspend fun getOrderPayments(): List<OrderPayment>
    suspend fun getOrderPromos(): List<OrderPromo>
    suspend fun getOrderProductVariants(): List<OrderProductVariant>
    suspend fun updateOrder(order: Order)
    suspend fun insertOrders(list: List<Order>)
    suspend fun insertOrderDetails(list: List<OrderDetail>)
    suspend fun insertNewPaymentOrder(payment: OrderPayment)
    suspend fun insertNewPromoOrder(promo: OrderPromo)
    suspend fun migrateToUUID()
    suspend fun deleteAllOldOrders()
    suspend fun deleteAllNewOrders()
}
