package com.socialite.solite_pos.data.source.repository.impl

import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteQuery
import com.socialite.solite_pos.data.source.local.entity.room.master.Promo
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.data.source.local.room.PromosDao
import com.socialite.solite_pos.data.source.repository.PromosRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import com.socialite.solite_pos.data.source.local.entity.room.new_master.Promo as NewPromo

class PromosRepositoryImpl(
    private val dao: PromosDao,
    private val db: AppDatabase
) : PromosRepository {

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

    override suspend fun updatePromo(data: NewPromo) = dao.updateNewPromo(data)

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
}
