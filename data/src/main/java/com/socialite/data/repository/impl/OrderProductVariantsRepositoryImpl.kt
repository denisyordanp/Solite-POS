package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.new_bridge.OrderProductVariant
import com.socialite.core.database.AppDatabase
import com.socialite.core.database.dao.OrderDetailsDao
import com.socialite.core.database.dao.OrderProductVariantsDao
import com.socialite.core.database.dao.VariantOptionsDao
import com.socialite.data.repository.OrderProductVariantsRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import java.util.UUID
import javax.inject.Inject

class OrderProductVariantsRepositoryImpl @Inject constructor(
    private val dao: OrderProductVariantsDao,
    private val orderDetailsDao: OrderDetailsDao,
    private val variantOptionsDao: VariantOptionsDao,
    private val db: AppDatabase
) : OrderProductVariantsRepository {

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

    override suspend fun getItems(): List<OrderProductVariant> {
        return dao.getNewOrderProductVariants()
    }

    override suspend fun updateItems(items: List<OrderProductVariant>) {
        dao.updateOrderProductVariants(items)
    }

    override suspend fun insertItems(items: List<OrderProductVariant>) {
        dao.insertOrderProductVariants(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<OrderProductVariant>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
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
                    val newOrderProductVariant =
                        OrderProductVariant(
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
