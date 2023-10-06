package com.socialite.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.common.utility.di.IoDispatcher
import com.socialite.core.database.AppDatabase
import com.socialite.core.database.dao.PromosDao
import com.socialite.data.repository.PromosRepository
import com.socialite.data.repository.SyncRepository
import com.socialite.data.schema.helper.UpdateSynchronizations
import com.socialite.schema.database.EntityData
import com.socialite.schema.database.master.Promo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject
import com.socialite.schema.database.new_master.Promo as NewPromo

class PromosRepositoryImpl @Inject constructor(
    private val dao: PromosDao,
    private val db: AppDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PromosRepository, SyncRepository<NewPromo> {

    override suspend fun insertPromo(data: NewPromo) = dao.insertNewPromo(data)
    override suspend fun updatePromo(data: NewPromo) = dao.updateNewPromo(
        data.copy(
            isUploaded = false
        )
    )

    override suspend fun getNeedUploadPromos() = dao.getNeedUploadPromos()
    override fun getPromos(query: SupportSQLiteQuery) = dao.getNewPromos(query).flowOn(dispatcher)
    override suspend fun migrateToUUID() {
        val promos = dao.getPromos(Promo.filter(Promo.Status.ALL)).firstOrNull()
        if (!promos.isNullOrEmpty()) {
            db.withTransaction {
                for (promo in promos) {
                    val uuid = promo.new_id.ifEmpty {
                        val updatedPromo = promo.copy(
                            new_id = UUID.randomUUID().toString()
                        )
                        dao.updatePromo(updatedPromo)
                        updatedPromo.new_id
                    }

                    val newPromo = NewPromo(
                        id = uuid,
                        name = promo.name,
                        desc = promo.desc,
                        isCash = promo.isCash,
                        value = promo.value,
                        isActive = promo.isActive,
                        isUploaded = promo.isUploaded
                    )
                    dao.insertNewPromo(newPromo)
                }
            }
        }
    }

    override suspend fun deleteAllOldCustomers() {
        dao.deleteAllOldPromos()
    }

    override suspend fun deleteAllNewCustomers() {
        dao.deleteAllNewPromos()
    }

    override suspend fun getItems() = dao.getPromos()

    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSynchronization(missingItems: List<NewPromo>?) {
        val update = UpdateSynchronizations(this as SyncRepository<EntityData>)
        update.updates(missingItems)
    }

    override suspend fun insertItems(items: List<NewPromo>) {
        dao.insertPromos(items)
    }

    override suspend fun updateItems(items: List<NewPromo>) {
        dao.updateNewPromos(items)
    }
}
