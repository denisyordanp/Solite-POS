package com.socialite.solite_pos.data.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderDetail
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderMixProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderPayment
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.entity.room.bridge.OrderProductVariantMix
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailProductMixWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantMixOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.helper.OrderData
import com.socialite.solite_pos.data.source.local.entity.room.master.Order
import com.socialite.solite_pos.data.source.local.entity.room.master.Product
import com.socialite.solite_pos.data.source.local.entity.room.master.VariantOption
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = :status AND date(${Order.ORDER_DATE}) = date(:date)")
    fun getOrdersByStatus(status: Int, date: String): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.STATUS} = :status AND date(${Order.ORDER_DATE}) BETWEEN date(:from) AND date(:until)")
    fun getOrdersByStatus(status: Int, from: String, until: String): Flow<List<OrderData>>

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
    suspend fun getOrderByNo(orderNo: String): OrderData?

    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo")
    fun getDetailOrdersLiveData(orderNo: String): Flow<List<OrderDetail>>

    @Transaction
    @Query("SELECT * FROM '${Order.DB_NAME}' WHERE ${Order.NO} = :orderNo")
    fun getOrderPayment(orderNo: String): Flow<OrderData?>

    @Transaction
    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
    suspend fun getOrderVariants(idDetail: Long): DetailWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :idMix")
    suspend fun getOrderMixVariantsOption(idMix: Long): DetailProductMixWithVariantOption

    @Transaction
    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${OrderDetail.ID} = :idDetail")
    suspend fun getOrderVariantsMix(idDetail: Long): DetailWithVariantMixOption

    @Query("SELECT * FROM ${OrderDetail.DB_NAME} WHERE ${Order.NO} = :orderNo AND ${Product.ID} = :productId")
    fun getDetailOrders(orderNo: String, productId: Long): OrderDetail

    @Query("SELECT * FROM ${OrderProductVariantMix.DB_NAME} WHERE ${OrderDetail.ID} = :detailId AND ${Product.ID} = :productId")
    fun getOrderProductVariantMix(detailId: Long, productId: Long): OrderProductVariantMix

    @Query("SELECT * FROM ${OrderMixProductVariant.DB_NAME} WHERE ${OrderProductVariantMix.ID} = :orderVariantId AND ${VariantOption.ID} = :variantOptionId")
    fun getOrderMixProductVariant(
        orderVariantId: Long,
        variantOptionId: Long
    ): OrderMixProductVariant

    @Query("SELECT * FROM ${OrderProductVariant.DB_NAME} WHERE ${OrderDetail.ID} = :orderDetailId AND ${VariantOption.ID} = :variantOptionId")
    fun getVariantOrder(orderDetailId: Long, variantOptionId: Long): OrderProductVariant

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrders(order: List<Order>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDetailOrder(detail: OrderDetail): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertDetailOrders(detail: List<OrderDetail>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantOrder(variants: OrderProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantOrders(variants: List<OrderProductVariant>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMixVariantOrder(variants: OrderMixProductVariant): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMixVariantOrders(variants: List<OrderMixProductVariant>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixOrder(variants: OrderProductVariantMix): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVariantMixOrders(variants: List<OrderProductVariantMix>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPaymentOrders(payment: List<OrderPayment>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPaymentOrder(payment: OrderPayment): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPaymentOrder(payment: OrderPayment)

    @Update
    fun updatePaymentOrder(order: OrderPayment)

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    fun deleteOrderDetail(orderDetail: OrderDetail)

    @Delete
    fun deleteOrderProductVariantMix(variantMix: OrderProductVariantMix)

    @Delete
    fun deleteOrderMixProductVariant(orderMix: OrderMixProductVariant)

    @Delete
    fun deleteOrderVariant(orderVariant: OrderProductVariant)
}
