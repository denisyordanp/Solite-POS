package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail as NewOrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPayment as NewOrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant as NewOrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderPromo as NewOrderPromo
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailProductMixWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantMixOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Store
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Order as NewOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.STATUS} = :status AND date(${NewOrder.ORDER_DATE}) = date(:date)")
    fun getOrdersByStatus(status: Int, date: String): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.STATUS} = :status AND date(${NewOrder.ORDER_DATE}) = date(:date) AND ${Store.ID} = :store")
    fun getOrdersByStatus(status: Int, date: String, store: String): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.STATUS} = :status AND ${Store.ID} = :store AND date(${NewOrder.ORDER_DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOrdersByStatus(
        status: Int,
        from: String,
        until: String,
        store: String
    ): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.ID} = :orderId")
    suspend fun getOrderDataById(orderId: String): OrderData?

    @Transaction
    @Query("SELECT * FROM '${NewOrder.DB_NAME}' WHERE ${NewOrder.ID} = :orderId")
    fun getOrderData(orderId: String): Flow<OrderData?>

    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrder.ID} = :orderId")
    fun getDetailOrders(orderId: String): Flow<List<NewOrderDetail>>

    @Transaction
    @Query("SELECT * FROM ${NewOrderDetail.DB_NAME} WHERE ${NewOrderDetail.ID} = :idDetail")
    suspend fun getOrderVariants(idDetail: String): DetailWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
    suspend fun getOrderMixVariantsOption(idMix: Long): DetailProductMixWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
    suspend fun getOrderVariantsMix(idDetail: Long): DetailWithVariantMixOption

    @Query("SELECT * FROM '${Order.DB_NAME}'")
    suspend fun getOrders(): List<Order>

    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo LIMIT 1")
    suspend fun getOrderByNo(orderNo: String): Order?

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}'")
    suspend fun getOrderDetails(): List<OrderDetail>

    @Query("SELECT * FROM '${OrderDetail.DB_NAME}' WHERE ${OrderDetail.ID} = :orderDetailId LIMIT 1")
    suspend fun getOrderDetailById(orderDetailId: Long): OrderDetail?

    @Query("SELECT * FROM '${OrderPayment.DB_NAME}'")
    suspend fun getOrderPayments(): List<OrderPayment>

    @Query("SELECT * FROM '${OrderPromo.DB_NAME}'")
    suspend fun getOrderPromos(): List<OrderPromo>

    @Query("SELECT * FROM '${OrderProductVariant.DB_NAME}'")
    suspend fun getOrderProductVariants(): List<OrderProductVariant>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrder(order: NewOrder)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDetailOrder(detail: OrderDetail): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderDetail(detail: NewOrderDetail)

    @Query("DELETE FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
    suspend fun deleteDetailOrders(orderNo: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVariantOrder(variants: OrderProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPaymentOrder(payment: OrderPayment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderPayment(payment: NewOrderPayment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPromoOrder(promo: OrderPromo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderPromo(promo: NewOrderPromo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewOrderProductVariant(orderProductVariant: NewOrderProductVariant)

    @Update
    suspend fun updateOrder(order: Order)

    @Update
    suspend fun updateNewOrder(order: NewOrder)

    @Update
    suspend fun updateOrderDetail(orderDetail: OrderDetail)

    @Update
    suspend fun updateOrderPayment(orderPayment: OrderPayment)

    @Update
    suspend fun updateOrderPromo(orderPromo: OrderPromo)

    @Update
    suspend fun updateOrderProductVariant(orderProductVariant: OrderProductVariant)

    @Query("DELETE FROM '${Order.DB_NAME}'")
    suspend fun deleteAllOldOrders()

    @Query("DELETE FROM '${OrderDetail.DB_NAME}'")
    suspend fun deleteAllOldOrderDetails()

    @Query("DELETE FROM '${OrderPayment.DB_NAME}'")
    suspend fun deleteAllOldOrderPayments()

    @Query("DELETE FROM '${OrderPromo.DB_NAME}'")
    suspend fun deleteAllOldOrderPromos()

    @Query("DELETE FROM '${OrderProductVariant.DB_NAME}'")
    suspend fun deleteAllOldOrderProductVariants()

    @Query("DELETE FROM '${NewOrder.DB_NAME}'")
    suspend fun deleteAllNewOrders()

    @Query("DELETE FROM '${NewOrderDetail.DB_NAME}'")
    suspend fun deleteAllNewOrderDetails()

    @Query("DELETE FROM '${NewOrderPayment.DB_NAME}'")
    suspend fun deleteAllNewOrderPayments()

    @Query("DELETE FROM '${NewOrderPromo.DB_NAME}'")
    suspend fun deleteAllNewOrderPromos()

    @Query("DELETE FROM '${NewOrderProductVariant.DB_NAME}'")
    suspend fun deleteAllNewOrderProductVariants()
}
