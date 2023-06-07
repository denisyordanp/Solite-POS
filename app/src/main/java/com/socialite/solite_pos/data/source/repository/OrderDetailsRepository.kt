package com.socialite.solite_pos.data.source.repository

import com.socialite.solite_pos.data.source.local.entity.room.helper.DetailWithVariantOption
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderDetail
import kotlinx.coroutines.flow.Flow

interface OrderDetailsRepository : SyncRepository<OrderDetail> {
    suspend fun getNeedUploadOrderDetails(): List<OrderDetail>
    suspend fun insertOrderDetails(list: List<OrderDetail>)
    suspend fun insertOrderDetail(orderDetail: OrderDetail)
    suspend fun getDeletedOrderDetailIds(): List<String>
    fun getOrderDetailByIdOrder(orderId: String): Flow<List<OrderDetail>>
    suspend fun getOrderDetailWithVariants(idDetail: String): DetailWithVariantOption
    suspend fun deleteOrderDetailAndRelated(orderId: String)
    suspend fun migrateToUUID()
    suspend fun deleteAllDeletedOrderDetails()
}
