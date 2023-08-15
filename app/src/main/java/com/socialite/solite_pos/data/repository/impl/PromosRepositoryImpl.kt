package com.socialite.solite_pos.data.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.helper.EntityData
import com.socialite.solite_pos.data.schema.room.master.Promo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.repository.PromosRepository
import com.socialite.solite_pos.data.repository.SyncRepository
import com.socialite.solite_pos.utils.tools.UpdateSynchronizations
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import com.socialite.solite_pos.data.schema.room.new_master.Promo as NewPromo

class PromosRepositoryImpl @Inject constructor(
    private val dao: PromosDao,
    private val db: AppDatabase
) : PromosRepository, SyncRepository<NewPromo> {

    companion object {
        @Volatile
        private var INSTANCE: PromosRepositoryImpl? = null

        fun getInstance(
            dao: PromosDao,
            db: AppDatabase
        ): PromosRepositoryImpl {
            if (INSTANCE == null) {
                synchronized(PromosRepositoryImpl::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PromosRepositoryImpl(dao = dao, db = db)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    override suspend fun insertPromo(data: NewPromo) = dao.insertNewPromo(data)
    override suspend fun updatePromo(data: NewPromo) = dao.updateNewPromo(data.copy(
        isUploaded = false
    ))

    override suspend fun getNeedUploadPromos() = dao.getNeedUploadPromos()
    override fun getPromos(query: SupportSQLiteQuery) = dao.getNewPromos(query)
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
