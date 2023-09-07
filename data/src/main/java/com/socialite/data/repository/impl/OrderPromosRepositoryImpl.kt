package com.socialite.data.repository.impl

import androidx.room.withTransaction
import com.socialite.data.database.AppDatabase
import com.socialite.data.database.dao.OrderPromosDao
import com.socialite.data.database.dao.OrdersDao
import com.socialite.data.database.dao.PromosDao
import com.socialite.data.repository.OrderPromosRepository
import com.socialite.data.schema.room.EntityData
import com.socialite.data.schema.room.new_bridge.OrderPromo
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import java.util.UUID
import javax.inject.Inject

class OrderPromosRepositoryImpl @Inject constructor(
    private val dao: OrderPromosDao,
    private val ordersDao: OrdersDao,
    private val promosDao: PromosDao,
    private val db: AppDatabase
) : OrderPromosRepository {

    override suspend fun getNeedUploadOrderPromos() = dao.getNeedUploadOrderPromos()

    override suspend fun getItems(): List<OrderPromo> {
        return dao.getNewOrderPromos()
    }

    override suspend fun updateItems(items: List<OrderPromo>) {
        dao.updateOrderPromos(items)
    }

    override suspend fun insertItems(items: List<OrderPromo>) {
        dao.insertOrderPromos(items)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<OrderPromo>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun insertNewPromoOrder(promo: OrderPromo) = dao.insertNewOrderPromo(promo)
    override suspend fun migrateToUUID() {
        val orderPromos = dao.getOrderPromos()
        db.withTransaction {
            for (orderPromo in orderPromos) {
                val uuid = orderPromo.new_id.ifEmpty {
                    val updatedOrderPromo = orderPromo.copy(
                        new_id = UUID.randomUUID().toString()
                    )
                    dao.updateOrderPromo(updatedOrderPromo)
                    updatedOrderPromo.new_id
                }

                val order = ordersDao.getOrderByNo(orderPromo.orderNO)
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
    }
}
