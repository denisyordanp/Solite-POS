package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import com.socialite.solite_pos.data.source.local.entity.room.new_bridge.OrderProductVariant
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.OrderDetailsDao
import com.socialite.solite_pos.data.source.local.room.OrderProductVariantsDao
import com.socialite.solite_pos.data.source.local.room.VariantOptionsDao
import com.socialite.solite_pos.data.source.repository.OrderProductVariantsRepository
import java.util.UUID

class OrderProductVariantsRepositoryImpl(
    private val dao: OrderProductVariantsDao,
    private val orderDetailsDao: OrderDetailsDao,
    private val variantOptionsDao: VariantOptionsDao,
    private val db: AppDatabase
) : OrderProductVariantsRepository {

    companion object {
        @Volatile
        private var INSTANCE: OrderProductVariantsRepositoryImpl? = null

        fun getInstance(
            dao: OrderProductVariantsDao,
            orderDetailsDao: OrderDetailsDao,
            variantOptionsDao: VariantOptionsDao,
            db: AppDatabase
        ): OrderProductVariantsRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(OrderProductVariantsRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OrderProductVariantsRepositoryImpl(
                            dao = dao,
                            orderDetailsDao = orderDetailsDao,
                            variantOptionsDao = variantOptionsDao,
                            db = db
                        )
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun getNeedUploadOrderProductVariants() = dao.getNeedOrderProductVariants()

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

    override suspend fun migrateToUUID() {
        val orderProductVariants = dao.getOrderProductVariants()
        db.withTransaction {
            for (orderProductVariant in orderProductVariants) {
                val uuid = orderProductVariant.new_id.ifEmpty {
                    val updatedOrderProductVariant = orderProductVariant.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderProductVariant(updatedOrderProductVariant)
                    updatedOrderProductVariant.new_id
                }

                val orderDetail =
                    orderDetailsDao.getOrderDetailById(orderProductVariant.idOrderDetail)
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
}
